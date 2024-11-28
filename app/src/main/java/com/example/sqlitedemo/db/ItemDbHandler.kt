package com.example.sqlitedemo.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.FileObserver.CREATE
import android.util.Log
import com.example.sqlitedemo.database.DatabaseHelper
import java.sql.SQLException

class ItemDbHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "item.db"
        private const val DATABASE_VERSION = 10
        private const val TABLE_NAME = "data_table"
        private const val TB_ID = "tb_id"
        private const val COL_ID = "id"
        private const val COL_TITLE = "title"
        private const val COL_VIEW = "views"
        private const val COL_USERID = "userId"
        private const val COL_BODY = "body"
        private const val COL_DES = "description"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = """
           CREATE TABLE $TABLE_NAME (
           $TB_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COL_ID INTEGER,
            $COL_TITLE TEXT,
            $COL_VIEW INTEGER,
            $COL_USERID INTEGER
            )
        """
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 7) {
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS $TABLE_NAME(" +
                        "$TB_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "$COL_ID INTEGER," +
                        "$COL_TITLE TEXT," +
                        "$COL_VIEW INTEGER," +
                        "$COL_USERID INTEGER)"
            )
        }
        if (newVersion == 7) {
            db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COL_BODY INTEGER")
            db.execSQL("UPDATE data_table SET body = ? WHERE id = ? ")
            Log.e("Sqlite onUpgrate", "new version 7")
        }


        if (newVersion > 9) {
            try {
                db.execSQL(
                    """
                CREATE TABLE data_table_temp (
                $TB_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_ID INTEGER,
                $COL_TITLE TEXT,
                $COL_VIEW INTEGER,
                $COL_DES TEXT
                )
            """
                )
                db.execSQL(
                    """
                INSERT INTO data_table_temp (
                $TB_ID, $COL_ID, $COL_TITLE, $COL_VIEW, $COL_DES
                ) SELECT 
                $TB_ID, $COL_ID, $COL_TITLE, $COL_VIEW, $COL_DES
                FROM $TABLE_NAME
            """
                )
                db.execSQL("DROP TABLE $TABLE_NAME")
                db.execSQL("ALTER TABLE data_table_temp RENAME TO $TABLE_NAME")
                Log.e("SQLite", "Renamed column $COL_BODY to $COL_DES")
            } catch (e: SQLException) {
                Log.e("SQLite", "Error renaming column: ${e.message}")
            }
        }
    }


    fun renameColumn(oldName: String, newName: String) {
        val writableDb = this.writableDatabase
        writableDb.beginTransaction()
        try {
            val renameQuery = "ALTER TABLE $TABLE_NAME RENAME COLUMN $oldName TO $newName"
            writableDb.execSQL(renameQuery)
            writableDb.setTransactionSuccessful()
            Log.i("DatabaseInfo", "Table renamed from $oldName to $newName")
        } catch (e: SQLException) {
            Log.e("DatabaseError", "Error renaming table: ${e.message}")
        } finally {
            writableDb.endTransaction()
            writableDb.close()
        }
    }

    fun updateBodyColumnFromApi(dataList: List<Post>) {
        val writableDb = this.writableDatabase

        writableDb.beginTransaction()
        try {
            for (data in dataList) {
                // Update the body column for each row
                val values = ContentValues().apply {
                    put(COL_BODY, data.body)
                }

                val rowsUpdated = writableDb.update(
                    TABLE_NAME,
                    values,
                    "$COL_ID = ?",
                    arrayOf(data.id.toString())
                )

                if (rowsUpdated > 0) {
                    Log.d("DatabaseUpdate", "Updated body for ID: ${data.id}")
                } else {
                    Log.d("DatabaseUpdate", "No matching row found for ID: ${data.id}")
                }
            }
            writableDb.setTransactionSuccessful()
        } catch (e: SQLException) {
            Log.e("DatabaseError", "Error updating data: ${e.message}")
        } finally {
            writableDb.endTransaction()
            writableDb.close()
        }
    }

    fun insertData(dataList: List<Post>): Int {
        val writableDb = this.writableDatabase
        var count = 0

        writableDb.beginTransaction()
        try {
            for (data in dataList) {


                val cursor = writableDb.query(
                    TABLE_NAME,
                    null,
                    "$COL_ID = ?",
                    arrayOf(data.id.toString()),
                    null,
                    null,
                    null
                )
                val exists = cursor.moveToFirst()
                cursor.close()

                if (exists) continue

                // Insert data
                val values = ContentValues().apply {
                    put(COL_ID, data.id)
                    put(COL_TITLE, data.title)
                    put(COL_VIEW, data.views)
                    put(COL_USERID, data.userId)
                }
                writableDb.insert(TABLE_NAME, null, values)
                count++
            }
            writableDb.setTransactionSuccessful()
        } catch (e: SQLException) {
            Log.e("DatabaseError", "Error inserting data: ${e.message}")
        } finally {
            writableDb.endTransaction()
            writableDb.close()
        }
        return count

    }
}