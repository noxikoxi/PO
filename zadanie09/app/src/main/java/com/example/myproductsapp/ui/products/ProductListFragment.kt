package com.example.myproductsapp.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycategoriesapp.ui.products.ProductAdapter
import com.example.myproductsapp.databinding.FragmentProductListBinding

class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductListViewModel by viewModels()

    // ID kategorii
    private var categoryId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         arguments?.let {
             categoryId = it.getString("categoryId") // Jeśli używasz Safe Args, to będzie ProductListFragmentArgs.fromBundle(it).categoryId
         }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productsRecyclerView = binding.productsRecyclerView
        productsRecyclerView.layoutManager = LinearLayoutManager(context)

        // Dodaj separator
        val dividerItemDecoration = DividerItemDecoration(
            productsRecyclerView.context,
            (productsRecyclerView.layoutManager as LinearLayoutManager).orientation
        )
        productsRecyclerView.addItemDecoration(dividerItemDecoration)

        val productAdapter = ProductAdapter(
            onProductClick = { product ->
                Toast.makeText(context, "Wybrano produkt: ${product.name}", Toast.LENGTH_SHORT).show()
            },
            onAddToCartClick = { product ->
                Toast.makeText(context, "Dodano do koszyka: ${product.name}", Toast.LENGTH_SHORT).show()
            }
        )
        productsRecyclerView.adapter = productAdapter

        // Pobieramy produkty z ViewModelu (jeśli categoryId jest null, pobierze wszystkie)
        val products = viewModel.getProducts(categoryId)
        productAdapter.submitList(products)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}