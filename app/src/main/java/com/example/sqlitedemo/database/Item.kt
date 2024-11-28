package com.example.sqlitedemo.database

import com.example.sqlitedemo.Dimensions
import com.example.sqlitedemo.Meta
import com.example.sqlitedemo.Review

data class Item(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
)
data class Product(
    val availabilityStatus: String,
    val brand: String,
    val category: String,
    val description: String,
    val dimensions: Dimensions,
    val discountPercentage: Double,
    val id: Int,
    val images: List<String>,
    val meta: Meta,
    val minimumOrderQuantity: Int,
    val price: Double,
    val rating: Double,
    val returnPolicy: String,
    val reviews: List<Review>,
    val shippingInformation: String,
    val sku: String,
    val stock: Int,
    val tags: List<String>,
    val thumbnail: String,
    val title: String,
    val warrantyInformation: String,
    val weight: Int
)