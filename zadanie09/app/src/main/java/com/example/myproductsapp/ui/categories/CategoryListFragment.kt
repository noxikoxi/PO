package com.example.myproductsapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproductsapp.MyApplication
import com.example.myproductsapp.R
import com.example.myproductsapp.databinding.FragmentCategoryListBinding
import com.example.myproductsapp.repository.ProductRepository
import com.example.myproductsapp.ui.products.ProductListFragment
import com.example.myproductsapp.ui.products.ProductListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CategoryListFragment : Fragment() {

    // View Binding
    private var _binding: FragmentCategoryListBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private lateinit var viewModel: CategoryListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val productRepository = ProductRepository(MyApplication.realm)
        viewModel = CategoryListViewModel(productRepository)
    }

    // Metoda odpowiedzialna za "napompowanie" (inflating) layoutu XML fragmentu
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Konfiguracja RecyclerView
        val categoriesRecyclerView = binding.categoriesRecyclerView
        categoriesRecyclerView.layoutManager = LinearLayoutManager(context)

        val dividerItemDecoration = DividerItemDecoration(
            categoriesRecyclerView.context,
            (categoriesRecyclerView.layoutManager as LinearLayoutManager).orientation
        )
        categoriesRecyclerView.addItemDecoration(dividerItemDecoration)

        // Tworzymy instancję Adaptera
        val categoryAdapter = CategoryAdapter { category ->
            val bundle = Bundle()
            bundle.putString("categoryId", category._id.toHexString())
            findNavController().navigate(R.id.action_navigation_categories_to_navigation_products, bundle)
        }
        categoriesRecyclerView.adapter = categoryAdapter // Przypinamy adapter do RecyclerView

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.categories.collectLatest { categories ->
                categoryAdapter.submitList(categories)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}