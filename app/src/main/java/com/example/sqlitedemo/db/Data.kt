package com.example.sqlitedemo.db

import com.example.sqlitedemo.Reactions

data class Data(
    val limit: Int,
    val posts: List<Post>,
    val skip: Int,
    val total: Int
)
data class Post(
    val body: String,
    val id: Int,
    val reactions: Reactions,
    val tags: List<String>,
    val title: String,
    val userId: Int,
    val views: Int
)