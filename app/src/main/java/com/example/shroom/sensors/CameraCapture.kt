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

    /**
     * File the camera is currently writing to, or `null` when no capture
     * is in flight. Consumed by the caller once the capture succeeds.
     */
    var pendingFile: File? = null
        private set

    /**
     * Creates a new photo file in the app's private Pictures directory and
     * returns its shareable Uri, to pass as `EXTRA_OUTPUT` to the camera.
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
     * Transfers ownership of the pending photo to the caller and clears the
     * internal reference, so a later [clearPending] cannot delete it.
     */
    fun takePending(): File? {
        val file = pendingFile
        pendingFile = null
        return file
    }

    /** Discards a photo the camera never delivered (user cancelled / failure). */
    fun clearPending() {
        pendingFile?.delete()
        pendingFile = null
    }

    private companion object {
        val FILE_STAMP = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
    }
}
