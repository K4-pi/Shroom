package com.example.shroom

import android.app.Application
import android.content.Context
import org.osmdroid.config.Configuration
import java.io.File

class ShroomApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val prefs = getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
        val osmConfig = Configuration.getInstance()

        // Must identify the app — OSM blocks generic / library default agents with 403
        osmConfig.load(this, prefs)
        osmConfig.userAgentValue = "Shroom/${BuildConfig.VERSION_NAME} (${BuildConfig.APPLICATION_ID})"
        osmConfig.osmdroidBasePath = File(filesDir, "osmdroid")
        osmConfig.osmdroidTileCache = File(filesDir, "osmdroid/tiles")

        // Keep cached tiles for offline foraging (1 year)
        osmConfig.expirationOverrideDuration = 365L * 24 * 60 * 60 * 1000
        osmConfig.tileFileSystemCacheMaxBytes = 600L * 1024 * 1024
        osmConfig.tileFileSystemCacheTrimBytes = 500L * 1024 * 1024
    }
}
