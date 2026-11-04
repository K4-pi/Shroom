package com.example.shroom.sensors

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Prepares destination files for full-resolution camera captures taken via
 * [android.provider.MediaStore.ACTION_IMAGE_CAPTURE] and exposes them as
 * shareable `content://` Uris through [FileProvider].
 */
class CameraCapture(context: Context) {

    private val appContext = context.applicationContext

    var pendingFile: File? = null // `null` when no capture
        private set

    /**
     * @brief Creates a new photo file in the app's private Pictures directory.
     * @return Shareable content Uri to pass as EXTRA_OUTPUT to the camera.
     */
    fun newCaptureUri(): Uri {
        val dir = File(
            appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "points",
        ).apply { mkdirs() }

        val file = File(dir, "point_${FILE_STAMP.format(Date())}.jpg")
        pendingFile = file
        return FileProvider.getUriForFile(
            appContext,
            "${appContext.packageName}.fileprovider",
            file,
        )
    }

    /**
     * @brief Transfers ownership of the pending photo to the caller.
     * @return The pending file, or null when no capture is in flight.
     */
    fun takePending(): File? {
        val file = pendingFile
        pendingFile = null
        return file
    }

    /**
     * @brief Discards a photo the camera never delivered.
     * @return Nothing.
     */
    fun clearPending() {
        pendingFile?.delete()
        pendingFile = null
    }

    private companion object {
        val FILE_STAMP = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
    }
}
