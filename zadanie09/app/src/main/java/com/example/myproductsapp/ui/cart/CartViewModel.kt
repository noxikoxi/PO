package com.example.myproductsapp.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myproductsapp.models.CartItem
import com.example.myproductsapp.models.CartItemUI
import com.example.myproductsapp.models.Product
import com.example.myproductsapp.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItemUI>>(emptyList())
    val cartItems: StateFlow<List<CartItemUI>> = _cartItems.asStateFlow()

    val totalPrice: StateFlow<Double> = _cartItems
        .map { items -> items.sumOf { it.productPrice * it.quantity } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    init {
        viewModelScope.launch {
            cartRepository.getCartItems().collect { items ->
                _cartItems.value = items
            }
        }
    }

    fun addProductToCart(product: Product, quantity: Int = 1) {
        viewModelScope.launch {
            cartRepository.addOrUpdateProductInCart(product, quantity)
        }
    }

    fun updateCartItemQuantity(cartItemId: ObjectId, newQuantity: Int) {
        viewModelScope.launch {
            cartRepository.updateCartItemQuantity(cartItemId, newQuantity)
        }
    }

    fun removeCartItem(cartItemId: ObjectId) {
        viewModelScope.launch {
            cartRepository.removeCartItem(cartItemId)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }
}
