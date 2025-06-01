package com.example.myproductsapp.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproductsapp.MyApplication
import com.example.myproductsapp.databinding.FragmentCartBinding
import com.example.myproductsapp.repository.CartRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cartRepository = CartRepository(MyApplication.realm)
        viewModel = CartViewModel(cartRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cartRecyclerView = binding.cartRecyclerView
        cartRecyclerView.layoutManager = LinearLayoutManager(context)

        cartAdapter = CartAdapter(
            onQuantityChange = { cartItem, newQuantity ->
                viewModel.updateCartItemQuantity(cartItem.id, newQuantity)
            },
            onRemoveItem = { cartItem ->
                viewModel.removeCartItem(cartItem.id)
                Toast.makeText(context, "Usunięto ${cartItem.productName} z koszyka", Toast.LENGTH_SHORT).show()
            }
        )
        cartRecyclerView.adapter = cartAdapter

        // Obserwuj elementy koszyka z ViewModelu
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cartItems.collectLatest { cartItems ->
                cartAdapter.submitList(cartItems)
                // Pokaż/ukryj komunikat o pustym koszyku
                binding.emptyCartMessage.visibility = if (cartItems.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        // Obserwuj całkowitą cenę
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.totalPrice.collectLatest { price ->
                binding.totalPriceTextView.text = String.format("Suma: %.2f zł", price)
            }
        }

        // Obsługa przycisku "Wyczyść koszyk"
        binding.clearCartButton.setOnClickListener {
            viewModel.clearCart()
            Toast.makeText(context, "Koszyk został wyczyszczony", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
