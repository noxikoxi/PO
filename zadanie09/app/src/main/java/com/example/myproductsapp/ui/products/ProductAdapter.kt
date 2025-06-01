package com.example.myproductsapp.ui.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myproductsapp.R
import com.example.myproductsapp.models.Product

typealias OnAddToCartClick = (Product) -> Unit

class ProductAdapter(
    private val onAddToCartClick: OnAddToCartClick // Dodaj nową lambdę
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.product_name_text_view)
        val productDescription: TextView = itemView.findViewById(R.id.product_description_text_view)
        val productPrice: TextView = itemView.findViewById(R.id.product_price_text_view)
        val addToCartButton: Button = itemView.findViewById(R.id.add_to_cart_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.productName.text = product.name
        holder.productDescription.text = product.description
        holder.productPrice.text = String.format("%.2f zł", product.price)

        holder.addToCartButton.setOnClickListener {
            onAddToCartClick(product) // Wywołaj lambda z produktem
        }
    }

    private class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.name == newItem.name &&
                    oldItem.description == newItem.description &&
                    oldItem.price == newItem.price &&
                    oldItem.category?._id == newItem.category?._id
        }
    }
}
