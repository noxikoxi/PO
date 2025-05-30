package com.example.myproductsapp.ui.products

import androidx.lifecycle.ViewModel
import com.example.myproductsapp.models.Product
import com.example.myproductsapp.repository.DataSource

class ProductListViewModel : ViewModel() {

    fun getProducts(categoryId: String? = null): List<Product> {
        return DataSource.getAllProducts(categoryId)
    }
}