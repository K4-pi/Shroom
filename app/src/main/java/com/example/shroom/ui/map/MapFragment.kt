package com.example.shroom.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.shroom.R
import com.example.shroom.data.MushroomTome
import com.example.shroom.data.ShroomPoint
import com.example.shroom.data.ShroomPointDb
import com.example.shroom.databinding.FragmentMapBinding
import com.example.shroom.map.ShroomTileSources
import com.example.shroom.sensors.CameraCapture
import com.example.shroom.sensors.CompassSensor
import com.example.shroom.sensors.GeoFix
import com.example.shroom.sensors.LocationTracker
import com.example.shroom.util.isNetworkOnline
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.tileprovider.cachemanager.CacheManager
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Marker
import java.io.File

class MapFragment : Fragment() {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationTracker: LocationTracker
    private lateinit var compassSensor: CompassSensor
    private lateinit var cameraCapture: CameraCapture
    private lateinit var pointDb: ShroomPointDb
    private var youMarker: Marker? = null
    private val pointMarkers = mutableListOf<Marker>()
//    private var followMe = false
    private var cacheBusy = false
    private var lastFix: GeoFix? = null

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { result ->
        val granted =
            result[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) startLocationUpdates()
        updateStatusPanel()
    }

    /**
     * @brief Camera result handler: offers the species picker for saved
     * @brief captures and deletes the file otherwise.
     */
    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture(),
    ) { saved ->
        val file = cameraCapture.takePending()
        if (!isAdded) {
            file?.delete()
            return@registerForActivityResult
        }
        if (saved && file != null && file.exists()) {
            describePoint(file.absolutePath)
        } else {
            file?.delete()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationTracker = LocationTracker(requireContext())
        compassSensor = CompassSensor(requireContext())
        cameraCapture = CameraCapture(requireContext())
        pointDb = ShroomPointDb.get(requireContext())

        setupMap()
        binding.btnLocate.setOnClickListener { onLocateClicked() }
        binding.btnCache.setOnClickListener { onCacheClicked() }
        binding.btnPoint.setOnClickListener { onSavePointClicked() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                compassSensor.headingDegrees.collectLatest { heading ->
                    binding.compass.setHeading(heading)
                }
            }
        }

        if (hasLocationPermission()) {
            startLocationUpdates()
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
            )
        }
        loadSavedPoints()
        updateStatusPanel()
    }

    private fun setupMap() {
        binding.map.apply {
            setTileSource(ShroomTileSources.OSM_DE)
            setMultiTouchControls(true)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
            controller.setZoom(15.0)
            controller.setCenter(GeoPoint(50.08, 14.42))
            isTilesScaledToDpi = true
            maxZoomLevel = 19.0
            minZoomLevel = 3.0

            val marker = Marker(this).apply {
                title = "You"
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
            }
            overlays.add(marker)
            youMarker = marker
        }
    }

    private fun startLocationUpdates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (!locationTracker.hasPermission()) return@repeatOnLifecycle
                locationTracker.updates().collectLatest { fix ->
                    lastFix = fix
                    val point = GeoPoint(fix.latitude, fix.longitude)
                    youMarker?.apply {
                        position = point
                        setInfoWindow(null)
                    }
//                    if (followMe) {
//                        binding.map.controller.animateTo(point)
//                    }
                    binding.map.invalidate()
                    updateStatusPanel()
                }
            }
        }
    }

    private fun transformToUserLocation() {
        lastFix?.let {
            binding.map.controller.animateTo(GeoPoint(it.latitude, it.longitude))
        } ?: run {
            if (!hasLocationPermission()) {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                    ),
                )
            }
        }
    }

    private fun onLocateClicked() {
//        followMe = !followMe
//        if (followMe) transformToUserLocation()
        transformToUserLocation()
    }

    private fun onCacheClicked() {
        if (cacheBusy) return
        cacheBusy = true
        val map = binding.map
        Toast.makeText(requireContext(), R.string.caching_tiles, Toast.LENGTH_SHORT).show()
        CacheManager(map).downloadAreaAsync(
            requireContext(),
            map.boundingBox,
            map.zoomLevelDouble.toInt().coerceAtLeast(12),
            map.zoomLevelDouble.toInt().coerceAtMost(17).coerceAtLeast(12),
            object : CacheManager.CacheManagerCallback {
                override fun onTaskComplete() {
                    cacheBusy = false
                    if (!isAdded) return
                    Toast.makeText(requireContext(), R.string.cache_done, Toast.LENGTH_SHORT).show()
                }

                override fun updateProgress(
                    progress: Int,
                    currentZoomLevel: Int,
                    zoomMin: Int,
                    zoomMax: Int,
                ) = Unit

                override fun downloadStarted() = Unit

                override fun setPossibleTilesInArea(total: Int) = Unit

                override fun onTaskFailed(errors: Int) {
                    cacheBusy = false
                    if (!isAdded) return
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.cache_failed, errors),
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            },
        )
    }

    private fun updateStatusPanel() {
        if (_binding == null) return
        val online = requireContext().isNetworkOnline()
        binding.mapStatus.setText(if (online) R.string.map_online else R.string.map_offline)
        binding.offlineBadge.isVisible = !online

        val fix = lastFix
        binding.mapCoords.text = when {
            fix != null -> getString(
                R.string.coords_format,
                fix.latitude,
                fix.longitude,
                fix.accuracyMeters,
            )
            hasLocationPermission() -> getString(R.string.seeking_satellites)
            else -> getString(R.string.permission_location_rationale)
        }
    }

    private fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    /**
     * @brief Opens the capture-or-preset question for the current GPS fix.
     * @brief Requires a live location fix; shows a toast otherwise.
     * @return Nothing.
     */
    private fun onSavePointClicked() {
        if (lastFix == null) {
            Toast.makeText(requireContext(), R.string.point_needs_gps, Toast.LENGTH_SHORT).show()
            return
        }
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.point_photo_prompt)
            .setMessage(R.string.point_photo_message)
            .setPositiveButton(R.string.point_photo_yes) { _, _ ->
                takePictureLauncher.launch(cameraCapture.newCaptureUri())
            }
            .setNegativeButton(R.string.point_photo_no) { _, _ ->
                describePoint(photoPath = null)
            }
            .show()
    }

    /**
     * @brief Offers the tome's species to identify the newly captured find.
     * @brief Cancelling discards the photo taken for this point.
     * @param photoPath Captured photo used as the point image, or null to
     *                  keep the tome illustration.
     * @return Nothing.
     */
    private fun describePoint(photoPath: String?) {
        val tome = MushroomTome.all.toTypedArray()
        val labels = arrayOfNulls<String>(tome.size)
        tome.forEachIndexed { i, mushroom ->
            labels[i] = "${mushroom.commonName} · ${mushroom.latinName}"
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.point_pick_species)
            .setItems(labels) { _, which -> savePoint(tome[which].id, photoPath) }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                if (photoPath != null) File(photoPath).delete()
                Toast.makeText(requireContext(), R.string.point_discarded, Toast.LENGTH_SHORT)
                    .show()
            }            .show()
    }

    /**
     * @brief Persists the identified find at the current GPS fix and draws it.
     * @brief Storing happens off the main thread; the marker only shows on success.
     * @param mushroomId Tome id of the identified species.
     * @param photoPath Captured photo used as the point image, or null.
     * @return Nothing.
     */
    private fun savePoint(mushroomId: String, photoPath: String?) {
        val fix = lastFix ?: return

        viewLifecycleOwner.lifecycleScope.launch {
            val id = withContext(Dispatchers.IO) {
                pointDb.insert(
                    ShroomPoint(
                        id = 0,
                        mushroomId = mushroomId,
                        latitude = fix.latitude,
                        longitude = fix.longitude,
                        photoPath = photoPath,
                    ),
                )
            }
            if (!isAdded) return@launch
            addPointMarker(
                ShroomPoint(id, mushroomId, fix.latitude, fix.longitude, photoPath),
            )
            Toast.makeText(requireContext(), R.string.point_saved, Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadSavedPoints() {
        viewLifecycleOwner.lifecycleScope.launch {
            val points = withContext(Dispatchers.IO) { pointDb.all() }
            if (!isAdded) return@launch
            points.forEach(::addPointMarker)
        }
    }

    /**
     * @brief Adds a marker for a saved point and attaches its delete info window.
     * @param point Saved point drawn on the map.
     */
    private fun addPointMarker(point: ShroomPoint) {
        val mushroom = MushroomTome.byId(point.mushroomId)
        val marker = Marker(binding.map).apply {
            position = GeoPoint(point.latitude, point.longitude)
            title = mushroom?.commonName ?: point.mushroomId
            subDescription = mushroom?.latinName
            if (point.photoPath == null) {
                snippet = getString(R.string.point_preset_note)
            }
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        }
        marker.setInfoWindow(
            DeletePointInfoWindow(requireContext(), binding.map) { deletePoint(point, marker) },
        )
        binding.map.overlays.add(marker)
        pointMarkers += marker
        binding.map.invalidate()
    }

    /**
     * @brief Removes a saved point from the map and from the database,
     * @brief also deleting its captured photo if present.
     * @param point The saved point to remove.
     * @param marker The marker currently representing the point.
     * @return Nothing; the change is reflected on-screen and in the database.
     */
    private fun deletePoint(point: ShroomPoint, marker: Marker) {
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                pointDb.delete(point.id)
                point.photoPath?.let { File(it).delete() }
            }
            if (!isAdded) return@launch
            marker.closeInfoWindow()
            binding.map.overlays.remove(marker)
            pointMarkers.remove(marker)
            binding.map.invalidate()
            Toast.makeText(requireContext(), R.string.point_deleted, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
        updateStatusPanel()
    }

    override fun onPause() {
        binding.map.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        binding.map.onDetach()
        youMarker = null
        pointMarkers.clear()
        _binding = null
        super.onDestroyView()
    }
}
