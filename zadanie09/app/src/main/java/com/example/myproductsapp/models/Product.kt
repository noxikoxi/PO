package com.example.myproductsapp.models

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val categoryId: String, // Klucz do powiązania z kategorią
    val imageUrl: String? = null
)