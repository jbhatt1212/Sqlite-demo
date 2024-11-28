package com.example.sqlitedemo.db

import retrofit2.http.GET
import retrofit2.http.POST

interface ItemService {
//@GET("products/category/smartphones")
//suspend fun getItem() : Item
    @GET("posts/search?q=love")
    suspend fun  getData() : Data

}