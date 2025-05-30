package com.example.mycategoriesapp.ui.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myproductsapp.R
import com.example.myproductsapp.models.Product
import java.text.NumberFormat
import java.util.Locale

// Callback do obsługi kliknięć na produkt lub przycisk "Dodaj do koszyka"
class ProductAdapter(
    private val onProductClick: (Product) -> Unit,
    private val onAddToCartClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var products: List<Product> = emptyList()

    fun submitList(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false) // Używamy item_product.xml
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = products.size

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productNameTextView: TextView = itemView.findViewById(R.id.product_name_text_view)
        private val productDescriptionTextView: TextView = itemView.findViewById(R.id.product_description_text_view)
        private val productPriceTextView: TextView = itemView.findViewById(R.id.product_price_text_view)
        private val addToCartButton: Button = itemView.findViewById(R.id.add_to_cart_button)

        fun bind(product: Product) {
            productNameTextView.text = product.name
            productDescriptionTextView.text = product.description

            // Formatowanie ceny jako waluty
            val format = NumberFormat.getCurrencyInstance(Locale("pl", "PL")) // Polski format waluty
            productPriceTextView.text = format.format(product.price)

            // Obsługa kliknięcia na cały element produktu
            itemView.setOnClickListener {
                onProductClick(product)
            }

            // Obsługa kliknięcia na przycisk "Dodaj do koszyka"
            addToCartButton.setOnClickListener {
                onAddToCartClick(product)
            }
        }
    }
}