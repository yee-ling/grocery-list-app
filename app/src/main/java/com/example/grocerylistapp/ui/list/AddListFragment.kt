package com.example.grocerylistapp.ui.list

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerylistapp.R
import com.example.grocerylistapp.data.model.Category
import com.example.grocerylistapp.databinding.FragmentAddListBinding
import com.example.grocerylistapp.ui.adapter.ItemsAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class AddListFragment : Fragment() {
    private val viewModel: AddListViewModel by viewModels()
    private lateinit var binding: FragmentAddListBinding
    private lateinit var adapter: ItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupItemsAdapter()

        setFragmentResultListener("new_item") {_, bundle ->
            val itemName = bundle.getString("name") ?: ""
            val itemQuantity = bundle.getInt("quantity")
            val itemCategory = bundle.getString("category") ?: ""
            viewModel.addItem(itemName, itemQuantity, Category(name = itemCategory))
            adapter.setItems(viewModel.pendingItems)
        }

        lifecycleScope.launch {
            viewModel.error.collect {
                val snackbar = Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG)
                snackbar.setBackgroundTint(
                    ContextCompat.getColor(requireContext(), R.color.red)
                )
                snackbar.show()
            }
        }

        lifecycleScope.launch {
            viewModel.finish.collect() {
                setFragmentResult("manage_list", Bundle())
                findNavController().popBackStack(R.id.homeFragment, false )
            }
        }

        binding.mbAddItem.setOnClickListener {
            val action = AddListFragmentDirections.actionAddListToAddItem(-1)
            findNavController().navigate(action)
        }

        binding.run {
            fabSubmit.setOnClickListener {
                val name = etGroceryListName.text.toString()
                val desc = etGroceryListDesc.text.toString()
                viewModel.saveList(name, desc)
            }
        }
    }

    fun setupItemsAdapter() {
        adapter = ItemsAdapter(items = emptyList())
        binding.rvItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvItems.adapter = adapter
    }
}