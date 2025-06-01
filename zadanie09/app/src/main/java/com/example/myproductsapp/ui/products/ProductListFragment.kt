package com.example.myproductsapp.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycategoriesapp.ui.products.ProductAdapter
import com.example.myproductsapp.MyApplication
import com.example.myproductsapp.databinding.FragmentProductListBinding
import com.example.myproductsapp.repository.ProductRepository
import io.realm.kotlin.Realm
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProductListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val productRepository = ProductRepository(MyApplication.realm)
        var categoryIdFromArgs: String? = null

        arguments?.let {
            categoryIdFromArgs = it.getString("categoryId")
        }

        viewModel = ProductListViewModel(productRepository, categoryIdFromArgs)
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.products.collectLatest { productList ->
                productAdapter.submitList(productList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}