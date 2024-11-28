package com.example.sqlitedemo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DbHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "items.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "item_table"
        private const val COL_ID = "id"
        private const val COL_NAME = "name"
        private const val COL_AGE = "age"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
            $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COL_NAME TEXT NOT NULL,
            $COL_AGE TEXT
            )
        """
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
     db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
//    fun insertData(name: String, age: String):Long{
//     val db =  this.writableDatabase
//        val values = ContentValues().apply {
//            put(COL_NAME,name)
//            put(COL_AGE,age)
//        }
//        val result = db.insert(TABLE_NAME, null, values)
//        db.close()
//        return result
//    }
fun insertData(name: String, age: String): Long {
    val db = this.readableDatabase
    val cursor = db.rawQuery(
        "SELECT * FROM $TABLE_NAME WHERE $COL_NAME = ? OR $COL_AGE = ?",
        arrayOf(name, age)
    )

    val exists = cursor.moveToFirst()
    Log.e("exists","exists : $exists")

    cursor.close()

    if (exists) {
        db.close()
        return -1
    }

    val writableDb = this.writableDatabase
    val values = ContentValues().apply {
        put(COL_NAME, name)
        put(COL_AGE, age)
    }

    val result = writableDb.insert(TABLE_NAME, null, values)
    writableDb.close()
    return result
}

    fun getAllData(): List<ModelData>{
        val data = mutableListOf<ModelData>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val items = ModelData(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                    age = cursor.getInt(cursor.getColumnIndexOrThrow(COL_AGE)).toString(),
                )
                data.add(items)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return data
    }
    fun updateData(id: Int, name: String, age: String):Int{
    val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE ($COL_NAME = ? OR $COL_AGE = ? ) AND $COL_ID != ?",
            arrayOf(name, age, id.toString())
        )

        val exists = cursor.moveToFirst()
        cursor.close()

        if (exists) {
            db.close()
            return -1
        }
     val writeDb =this.writableDatabase
        val values = ContentValues().apply {
        put(COL_NAME,name)
        put(COL_AGE,age)
    }

        val result = db.update(TABLE_NAME, values, "$COL_ID=?", arrayOf(id.toString()))

        writeDb.close()
        return result
    }

    fun deleteCourse(id: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(
            TABLE_NAME,
            "$COL_ID=?",
            arrayOf(id.toString())
        )
        db.close()
        return result
    }
}
