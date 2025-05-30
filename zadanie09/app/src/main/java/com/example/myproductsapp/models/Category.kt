package com.example.myproductsapp.models

data class Category(
    val id: String,
    val name: String,
    val imageUrl: String? = null // Opcjonalny URL obrazka
)