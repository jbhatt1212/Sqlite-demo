package com.example.sqlitedemo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "courses.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "Course"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "course_name"
        private const val COLUMN_DURATION = "course_duration"
        private const val COLUMN_TRACKS = "course_tracks"
        private const val COLUMN_DESCRIPTION = "course_description"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_DURATION TEXT,
                $COLUMN_TRACKS TEXT,
                $COLUMN_DESCRIPTION TEXT
            )
        """
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Insert a new course
    fun insertCourse(
        name: String,
        duration: String,
        tracks: String,
        description: String
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_DURATION, duration)
            put(COLUMN_TRACKS, tracks)
            put(COLUMN_DESCRIPTION, description)
        }
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result
    }

    // Read all courses
    fun getAllCourses(): List<Course> {
        val courses = mutableListOf<Course>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val course = Course(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    duration = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DURATION)),
                    tracks = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TRACKS)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                )
                courses.add(course)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return courses
    }

    // Update a course
    fun updateCourse(
        id: Int,
        name: String,
        duration: String,
        tracks: String,
        description: String
    ): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_DURATION, duration)
            put(COLUMN_TRACKS, tracks)
            put(COLUMN_DESCRIPTION, description)
        }
        val result = db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return result
    }

    // Delete a course
    fun deleteCourse(id: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return result
    }
}