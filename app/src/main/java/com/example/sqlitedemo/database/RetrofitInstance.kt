package com.example.sqlitedemo.database

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val interceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BASIC)
    }
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HeaderInterceptor())
        .addInterceptor(RetrofitInstance.interceptor)
        .addInterceptor(APIKeyInterceptor())
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://dummyjson.com/")
        .client(RetrofitInstance.okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getService(): ItemService = RetrofitInstance.retrofit.create(ItemService::class.java)
}
// https://dummyjson.com/products/category/smartphones
// https://dummyjson.com/posts/search?q=love