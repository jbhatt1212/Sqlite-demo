package com.example.sqlitedemo.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.TextUtils
import android.util.Log
import com.example.sqlitedemo.database.TableEntry.COL_ID
import com.example.sqlitedemo.database.TableEntry.COL_TITLE
import com.example.sqlitedemo.database.TableEntry.TABLE_NAME
import org.jetbrains.annotations.VisibleForTesting
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.sql.SQLException


class DatabaseHelper private constructor(private val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TableEntry.SQL_CREATE_ENTRY_TABLE)

        // The rest of your create scripts go here.
    }

    fun insertItem(dataList: List<Product>): Int {
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
                    put(COL_TITLE,data.title)

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

    @SuppressLint("DefaultLocale")
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.e(
            TAG,
            "Updating table from $oldVersion to $newVersion"
        )
        // You will not need to modify this unless you need to do some android specific things.
        // When upgrading the database, all you need to do is add a file to the assets folder and name it:
        // from_1_to_2.sql with the version that you are upgrading to as the last version.
        for (i in oldVersion until newVersion) {
            val migrationName = String.format("from_%d_to_%d.sql", i, (i + 1))
            Log.d(
                TAG,
                "Looking for migration file: $migrationName"
            )
            readAndExecuteSQLScript(db, context, migrationName)
        }
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    private fun readAndExecuteSQLScript(db: SQLiteDatabase, ctx: Context, fileName: String) {
        if (TextUtils.isEmpty(fileName)) {
            Log.d(TAG, "SQL script file name is empty")
            return
        }

        Log.d(TAG, "Script found. Executing...")
        val assetManager = ctx.assets
        var reader: BufferedReader? = null

        try {
            val `is` = assetManager.open(fileName)
            val isr = InputStreamReader(`is`)
            reader = BufferedReader(isr)
            executeSQLScript(db, reader)
        } catch (e: IOException) {
            Log.e(TAG, "IOException:", e)
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    Log.e(TAG, "IOException:", e)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun executeSQLScript(db: SQLiteDatabase, reader: BufferedReader) {
        var line: String
        var statement = StringBuilder()
        while ((reader.readLine().also { line = it }) != null) {
            statement.append(line)
            statement.append("\n")
            if (line.endsWith(";")) {
                db.execSQL(statement.toString())
                statement = StringBuilder()
            }
        }
    }

    companion object {
        const val DATABASE_VERSION: Int = 2

        const val DATABASE_NAME: String = "database.db"
        private val TAG: String = DatabaseHelper::class.java.name

        private var mInstance: DatabaseHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): DatabaseHelper? {
            if (mInstance == null) {
                mInstance = DatabaseHelper(ctx.applicationContext)
            }
            return mInstance
        }

        @VisibleForTesting
        fun clearInstance() {
            mInstance = null
        }
    }
}