package com.example.myproductsapp.ui.cart

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myproductsapp.R
import com.example.myproductsapp.models.CartItem
import com.example.myproductsapp.models.CartItemUI

typealias OnQuantityChange = (CartItemUI, Int) -> Unit
typealias OnRemoveItem = (CartItemUI) -> Unit

class CartAdapter(
    private val onQuantityChange: OnQuantityChange,
    private val onRemoveItem: OnRemoveItem
) : ListAdapter<CartItemUI, CartAdapter.CartViewHolder>(CartItemDiffCallback()) {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.cart_product_name)
        val productPrice: TextView = itemView.findViewById(R.id.cart_product_price)
        val productQuantity: TextView = itemView.findViewById(R.id.cart_product_quantity)
        val decreaseButton: Button = itemView.findViewById(R.id.button_decrease_quantity)
        val increaseButton: Button = itemView.findViewById(R.id.button_increase_quantity)
        val removeButton: Button = itemView.findViewById(R.id.button_remove_item)

        fun bind(
            cartItem: CartItemUI,
            onQuantityChange: OnQuantityChange,
            onRemoveItem: OnRemoveItem
        ) {
            productName.text = cartItem.productName
            productPrice.text = String.format("Cena: %.2f zł", cartItem.productPrice)
            productQuantity.text = cartItem.quantity.toString()

            decreaseButton.setOnClickListener {
                onQuantityChange(cartItem, cartItem.quantity - 1)
            }
            increaseButton.setOnClickListener {
                onQuantityChange(cartItem, cartItem.quantity + 1)
            }
            removeButton.setOnClickListener {
                onRemoveItem(cartItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart_product, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = getItem(position)
        holder.bind(cartItem, onQuantityChange, onRemoveItem)
    }

    private class CartItemDiffCallback : DiffUtil.ItemCallback<CartItemUI>() {
        override fun areItemsTheSame(oldItem: CartItemUI, newItem: CartItemUI): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CartItemUI, newItem: CartItemUI): Boolean {
            return oldItem == newItem
        }
    }
}
