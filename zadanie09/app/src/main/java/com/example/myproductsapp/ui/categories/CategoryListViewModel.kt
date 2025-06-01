package com.example.myproductsapp.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproductsapp.models.Category
import com.example.myproductsapp.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryListViewModel(private val productRepository: ProductRepository) : ViewModel() {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    init {
        // Inicjalizujemy pobieranie kategorii, gdy ViewModel jest tworzony
        // Używamy viewModelScope, aby korutyna była powiązana z cyklem życia ViewModelu
        viewModelScope.launch {
            productRepository.getAllCategories().collect { categoryList ->
                // Kiedy otrzymamy nową listę kategorii z Realma, aktualizujemy StateFlow
                _categories.value = categoryList
            }
        }
    }
}