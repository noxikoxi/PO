package com.example.myproductsapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myproductsapp.models.Category
import com.example.myproductsapp.repository.DataSource

class CategoryListViewModel : ViewModel() {
    fun getCategories(): List<Category> {
        return DataSource.getCategories()
    }
}