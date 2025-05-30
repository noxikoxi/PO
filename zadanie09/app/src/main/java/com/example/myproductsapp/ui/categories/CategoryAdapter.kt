package com.example.myproductsapp.ui.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myproductsapp.R
import com.example.myproductsapp.models.Category

// Callback do obsługi kliknięć na kategorię (na razie tylko Toast)
class CategoryAdapter(private val onCategoryClick: (Category) -> Unit) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var categories: List<Category> = emptyList()

    // Metoda do aktualizacji danych w adapterze
    fun submitList(newCategories: List<Category>) {
        categories = newCategories
        notifyDataSetChanged()
    }

    // Tworzy i zwraca nowy ViewHolder (czyli "opakowanie" dla widoku pojedynczego elementu)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        // "Nadmuchuje" (inflates) layout item_category.xml
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    // Wiąże dane z View Holderem (czyli ustawia tekst, obrazki itp. dla konkretnego elementu listy)
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
    }

    // Zwraca całkowitą liczbę elementów na liście
    override fun getItemCount(): Int = categories.size

    // Wewnętrzna klasa ViewHolder przechowująca referencje do widoków w item_category.xml
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryNameTextView: TextView = itemView.findViewById(R.id.category_name_text_view)

        fun bind(category: Category) {
            categoryNameTextView.text = category.name // Ustawia nazwę kategorii w TextView
            itemView.setOnClickListener {
                onCategoryClick(category) // Wywołuje callback, gdy użytkownik kliknie element
            }
        }
    }
}