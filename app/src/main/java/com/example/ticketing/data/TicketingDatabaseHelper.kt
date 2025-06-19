package com.example.ticketing.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TicketingDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TicketingSystem.db"

        // Table names
        const val TABLE_QUEUE = "queue"
        const val TABLE_SETTINGS = "settings"

        // Common column names
        const val COLUMN_ID = "id"
        
        // Queue table columns
        const val COLUMN_NIK = "nik"
        const val COLUMN_NAME = "name"
        const val COLUMN_POLY = "poly"
        const val COLUMN_NEED = "need"
        const val COLUMN_QUEUE_NUMBER = "queue_number"
        const val COLUMN_STATUS = "status"
        const val COLUMN_CREATED_AT = "created_at"
        
        // Status values
        const val STATUS_WAITING = "waiting"
        const val STATUS_CALLED = "called"
        const val STATUS_DONE = "done"
        
        // Poly types
        const val POLY_GENERAL = "Poli Umum"
        const val POLY_DENTAL = "Poli Gigi"
        const val POLY_KIA = "Poli KIA"
        
        // Settings table columns
        const val COLUMN_SETTING_KEY = "key"
        const val COLUMN_SETTING_VALUE = "value"
        
        // Setting keys
        const val SETTING_GENERAL_COUNTER = "general_counter"
        const val SETTING_DENTAL_COUNTER = "dental_counter"
        const val SETTING_KIA_COUNTER = "kia_counter"
        const val SETTING_STAFF_PIN = "staff_pin"
        
        // Default PIN
        const val DEFAULT_PIN = "123456"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create queue table
        val createQueueTable = """
            CREATE TABLE $TABLE_QUEUE (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NIK TEXT NOT NULL,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_POLY TEXT NOT NULL,
                $COLUMN_NEED TEXT NOT NULL,
                $COLUMN_QUEUE_NUMBER TEXT NOT NULL,
                $COLUMN_STATUS TEXT NOT NULL,
                $COLUMN_CREATED_AT INTEGER NOT NULL
            )
        """.trimIndent()
        
        // Create settings table
        val createSettingsTable = """
            CREATE TABLE $TABLE_SETTINGS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SETTING_KEY TEXT UNIQUE NOT NULL,
                $COLUMN_SETTING_VALUE TEXT NOT NULL
            )
        """.trimIndent()
        
        db.execSQL(createQueueTable)
        db.execSQL(createSettingsTable)
        
        // Initialize settings with default values
        initializeSettings(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS $TABLE_QUEUE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SETTINGS")
        
        // Create tables again
        onCreate(db)
    }
    
    private fun initializeSettings(db: SQLiteDatabase) {
        // Initialize counters for each poly
        insertSetting(db, SETTING_GENERAL_COUNTER, "0")
        insertSetting(db, SETTING_DENTAL_COUNTER, "0")
        insertSetting(db, SETTING_KIA_COUNTER, "0")
        
        // Set default PIN for staff
        insertSetting(db, SETTING_STAFF_PIN, DEFAULT_PIN)
    }
    
    private fun insertSetting(db: SQLiteDatabase, key: String, value: String) {
        val values = ContentValues().apply {
            put(COLUMN_SETTING_KEY, key)
            put(COLUMN_SETTING_VALUE, value)
        }
        db.insert(TABLE_SETTINGS, null, values)
    }
    
    // Get setting value by key
    fun getSetting(key: String): String {
        val db = this.readableDatabase
        var value = ""
        
        val cursor = db.query(
            TABLE_SETTINGS,
            arrayOf(COLUMN_SETTING_VALUE),
            "$COLUMN_SETTING_KEY = ?",
            arrayOf(key),
            null,
            null,
            null
        )
        
        if (cursor.moveToFirst()) {
            value = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SETTING_VALUE))
        }
        
        cursor.close()
        return value
    }
    
    // Update setting value
    fun updateSetting(key: String, value: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_SETTING_VALUE, value)
        }
        
        val result = db.update(
            TABLE_SETTINGS,
            values,
            "$COLUMN_SETTING_KEY = ?",
            arrayOf(key)
        )
        
        return result > 0
    }
    
    // Generate and get next queue number for a poly
    fun getNextQueueNumber(poly: String): String {
        val settingKey = when (poly) {
            POLY_GENERAL -> SETTING_GENERAL_COUNTER
            POLY_DENTAL -> SETTING_DENTAL_COUNTER
            POLY_KIA -> SETTING_KIA_COUNTER
            else -> SETTING_GENERAL_COUNTER
        }
        
        val currentCounter = getSetting(settingKey).toInt()
        val nextCounter = currentCounter + 1
        
        // Update counter in settings
        updateSetting(settingKey, nextCounter.toString())
        
        // Format queue number based on poly type
        val prefix = when (poly) {
            POLY_GENERAL -> "U"
            POLY_DENTAL -> "G"
            POLY_KIA -> "K"
            else -> "X"
        }
        
        return "$prefix${String.format("%03d", nextCounter)}"
    }
    
    // Insert new queue
    fun insertQueue(nik: String, name: String, poly: String, need: String): Long {
        val db = this.writableDatabase
        val queueNumber = getNextQueueNumber(poly)
        val currentTimeMillis = System.currentTimeMillis()
        
        val values = ContentValues().apply {
            put(COLUMN_NIK, nik)
            put(COLUMN_NAME, name)
            put(COLUMN_POLY, poly)
            put(COLUMN_NEED, need)
            put(COLUMN_QUEUE_NUMBER, queueNumber)
            put(COLUMN_STATUS, STATUS_WAITING)
            put(COLUMN_CREATED_AT, currentTimeMillis)
        }
        
        return db.insert(TABLE_QUEUE, null, values)
    }
    
    // Get queue by ID
    fun getQueueById(id: Long): Cursor {
        val db = this.readableDatabase
        
        return db.query(
            TABLE_QUEUE,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
    }
    
    // Get all active queues for a specific poly
    fun getActiveQueuesForPoly(poly: String): Cursor {
        val db = this.readableDatabase
        
        return db.query(
            TABLE_QUEUE,
            null,
            "$COLUMN_POLY = ? AND $COLUMN_STATUS != ?",
            arrayOf(poly, STATUS_DONE),
            null,
            null,
            "$COLUMN_CREATED_AT ASC"
        )
    }
    
    // Update queue status
    fun updateQueueStatus(id: Long, status: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_STATUS, status)
        }
        
        val result = db.update(
            TABLE_QUEUE,
            values,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
        
        return result > 0
    }
    
    // Verify staff PIN
    fun verifyStaffPin(pin: String): Boolean {
        val storedPin = getSetting(SETTING_STAFF_PIN)
        return pin == storedPin
    }
} 