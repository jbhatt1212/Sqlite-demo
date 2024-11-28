package com.example.sqlitedemo.database

import retrofit2.http.GET
import retrofit2.http.POST

interface ItemService {
@GET("products/category/smartphones")
suspend fun getItem() : Item


}