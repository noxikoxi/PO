package com.example.myproductsapp.repository

import com.example.myproductsapp.models.Cart
import com.example.myproductsapp.models.CartItem
import com.example.myproductsapp.models.CartItemUI
import com.example.myproductsapp.models.Product
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class CartRepository(private val realm: Realm) {

    fun getCartItems(): Flow<List<CartItemUI>> {
        return realm.query<CartItem>().asFlow().map { changes ->
            changes.list.map {
                CartItemUI(
                    id = it._id,
                    productName = it.product?.name ?: "Nieznany",
                    productPrice = it.product?.price ?: 0.0,
                    quantity = it.quantity
                )
            }
        }
    }

    suspend fun addOrUpdateProductInCart(product: Product, quantity: Int) {
        realm.write {
            var cart = query<Cart>().first().find()
            if (cart == null) {
                cart = copyToRealm(Cart())
            }

            val existingCartItem = cart.items.query<CartItem>("product._id == $0", product._id).first().find()

            if (existingCartItem != null) {
                existingCartItem.quantity += quantity
            } else {
                // Produkt nie istnieje, dodaj nowy element do koszyka
                val newCartItem = copyToRealm(CartItem().apply {
                    this.product = findLatest(product)
                    this.quantity = quantity
                })
                cart.items.add(newCartItem)
            }
        }
    }

    // Aktualizacja ilości konkretnego produktu w koszyku
    suspend fun updateCartItemQuantity(cartItemId: ObjectId, newQuantity: Int) {
        realm.write {
            val cartItem = query<CartItem>("_id == $0", cartItemId).first().find()
            if (cartItem != null) {
                if (newQuantity <= 0) {
                    delete(cartItem)
                } else {
                    cartItem.quantity = newQuantity
                }
            }
        }
    }

    // Usuwanie produktu z koszyka
    suspend fun removeCartItem(cartItemId: ObjectId) {
        realm.write {
            val cartItem = query<CartItem>("_id == $0", cartItemId).first().find()
            if (cartItem != null) {
                delete(cartItem)
            }
        }
    }

    // Czyszczenie całego koszyka
    suspend fun clearCart() {
        realm.write {
            val cart = query<Cart>().first().find()
            if (cart != null) {
                delete(cart.items)
            }
        }
    }
}
