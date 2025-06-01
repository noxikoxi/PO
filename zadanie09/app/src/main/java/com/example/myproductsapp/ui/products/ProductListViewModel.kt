package com.example.myproductsapp.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproductsapp.models.Product
import com.example.myproductsapp.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductListViewModel(private val productRepository: ProductRepository, initialCategoryId: String?) : ViewModel() {

    // Używamy StateFlow do wystawiania listy produktów, aby UI mógł na nią reagować
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow(initialCategoryId)

    init {
        // Inicjalizujemy pobieranie produktów, gdy ViewModel jest tworzony
        // Używamy viewModelScope, aby korutyna była powiązana z cyklem życia ViewModelu
        viewModelScope.launch {
            _selectedCategoryId.collect { categoryId ->
                if (categoryId == null) {
                    productRepository.getAllProducts().collect { productList ->
                        _products.value = productList
                    }
                } else {
                    productRepository.getAllProductsByCategory(categoryId).collect { productList ->
                        _products.value = productList
                    }
                }
            }
        }
    }
}
