package com.example.shroom.map

import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.MapTileIndex

/**
 * Free raster tiles — no API key.
 *
 * Nominatim (nominatim.openstreetmap.org) is a *geocoder* (place search), not a map
 * tile server; it cannot paint the map. These endpoints serve the actual imagery.
 */
object ShroomTileSources {
    /**
     * OpenStreetMap Deutschland mirror — free, no key, standard OSM look.
     * https://www.openstreetmap.de/
     */
    val OSM_DE = object : XYTileSource(
        "OSM_DE",
        0,
        19,
        256,
        ".png",
        arrayOf("https://tile.openstreetmap.de/"),
        "© OpenStreetMap contributors",
    ) {
        override fun getTileURLString(pMapTileIndex: Long): String {
            return baseUrl +
                MapTileIndex.getZoom(pMapTileIndex) + "/" +
                MapTileIndex.getX(pMapTileIndex) + "/" +
                MapTileIndex.getY(pMapTileIndex) +
                mImageFilenameEnding
        }
    }

    /** Fallback community mirror (OSM France). */
    val OSM_FR = object : XYTileSource(
        "OSM_FR",
        0,
        20,
        256,
        ".png",
        arrayOf(
            "https://a.tile.openstreetmap.fr/osmfr/",
            "https://b.tile.openstreetmap.fr/osmfr/",
            "https://c.tile.openstreetmap.fr/osmfr/",
        ),
        "© OpenStreetMap contributors",
    ) {
        override fun getTileURLString(pMapTileIndex: Long): String {
            return baseUrl +
                MapTileIndex.getZoom(pMapTileIndex) + "/" +
                MapTileIndex.getX(pMapTileIndex) + "/" +
                MapTileIndex.getY(pMapTileIndex) +
                mImageFilenameEnding
        }
    }
}
