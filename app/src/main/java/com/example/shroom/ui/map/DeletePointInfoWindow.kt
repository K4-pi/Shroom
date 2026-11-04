package com.example.shroom.ui.map

import android.content.Context
import android.view.LayoutInflater
import com.example.shroom.R
import com.example.shroom.databinding.InfoWindowDeletePointBinding
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.OverlayWithIW
import org.osmdroid.views.overlay.infowindow.InfoWindow

/**
 * @brief Info window shown above a saved point's marker, offering a delete action.
 * @param context Context used to inflate the window's content view.
 * @param mapView Map view this window belongs to.
 * @param onDelete Invoked when the user presses the window's delete control.
 */
class DeletePointInfoWindow(
    context: Context,
    mapView: MapView,
    private val onDelete: () -> Unit,
) : InfoWindow(
    InfoWindowDeletePointBinding.inflate(LayoutInflater.from(context)).root,
    mapView,
) {
    private val windowBinding = InfoWindowDeletePointBinding.bind(mView!!)

    /**
     * @brief Fills the window's texts from the marker it was opened for
     *        and binds the delete control.
     * @param item Saved point's marker.
     */
    override fun onOpen(item: Any?) {
        val marker = item as? OverlayWithIW ?: return
        windowBinding.twiwTitle.text = marker.title
        windowBinding.twiwLatin.text = marker.subDescription
        windowBinding.btnwiwiDelete.setImageResource(R.drawable.ic_close)
        windowBinding.btnwiwiDelete.setOnClickListener { onDelete() }
    }

    /**
     * @brief Tear-down hook called when the window closes.
     */
    override fun onClose() = Unit
}
