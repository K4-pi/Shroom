package com.example.shroom.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

/**
 * A mushroom the forager marked on the map. `photoPath == null` means the
 * point uses the tome illustration of the linked [Mushroom]. Names and
 * edibility are resolved through [MushroomTome.byId] at display time.
 */
data class ShroomPoint(
    val id: Long,
    val mushroomId: String,
    val latitude: Double,
    val longitude: Double,
    val photoPath: String?,
)

/**
 * Embedded SQLite store for map points. Kept dependency-free
 * (no Room) on purpose — a single small table doesn't need a mapping layer.
 */
class ShroomPointDb private constructor(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    fun insert(point: ShroomPoint): Long = writableDatabase.use { db ->
        db.insertOrThrow(
            TABLE,
            null,
            ContentValues().apply {
                put(COL_MUSHROOM_ID, point.mushroomId)
                put(COL_LAT, point.latitude)
                put(COL_LON, point.longitude)
                put(COL_PHOTO, point.photoPath)
            },
        )
    }

    fun all(): List<ShroomPoint> = readableDatabase.use { db ->
        db.query(
            TABLE,
            arrayOf(BaseColumns._ID, COL_MUSHROOM_ID, COL_LAT, COL_LON, COL_PHOTO),
            null,
            null,
            null,
            null,
            "${BaseColumns._ID} DESC",
        ).use { cursor ->
            buildList {
                while (cursor.moveToNext()) {
                    add(
                        ShroomPoint(
                            id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)),
                            mushroomId = cursor.getString(cursor.getColumnIndexOrThrow(COL_MUSHROOM_ID)),
                            latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LAT)),
                            longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_LON)),
                            photoPath = cursor.getString(cursor.getColumnIndexOrThrow(COL_PHOTO)),
                        ),
                    )
                }
            }
        }
    }

    /**
     * @brief Deletes a stored map point by its database id.
     * @param id Row identifier as returned by [insert].
     * @return True when a row was actually deleted.
     */
    fun delete(id: Long): Boolean = writableDatabase.use { db ->
        db.delete(TABLE, "${BaseColumns._ID} = ?", arrayOf(id.toString())) > 0
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE (
                ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_MUSHROOM_ID TEXT NOT NULL,
                $COL_LAT REAL NOT NULL,
                $COL_LON REAL NOT NULL,
                $COL_PHOTO TEXT
            )
            """.trimIndent(),
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Schema evolution is small so far: recreating is fine. Points were
        // already visible on the live map, so a wipe costs little.
        db.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }

    companion object {
        private const val DB_VERSION = 2

        private const val DB_NAME = "shroom_points.db"
        private const val TABLE = "points"
        private const val COL_MUSHROOM_ID = "mushroom_id"
        private const val COL_LAT = "latitude"
        private const val COL_LON = "longitude"
        private const val COL_PHOTO = "photo_path"

        @Volatile
        private var instance: ShroomPointDb? = null

        fun get(context: Context): ShroomPointDb =
            instance ?: synchronized(this) {
                instance
                    ?: ShroomPointDb(context.applicationContext).also { instance = it }
            }
    }
}
