package com.example.sqlitedemo.database

import android.provider.BaseColumns

object TableEntry:BaseColumns {
    const val TABLE_NAME = "product_table"
    const val COL_ID = "id"
    const val COL_TITLE = "title"
    const val COL_BRAND = "brand"
    const val COL_CATEGORY = "category"

    const val SQL_CREATE_ENTRY_TABLE = """
        CREATE TABLE $TABLE_NAME (
            ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, 
             $COL_ID INTEGER,
             $COL_TITLE TEXT,
             $COL_BRAND TEXT,
             $COL_CATEGORY TEXT
        )
    """
}