package com.example.grocerylistapp.ui.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerylistapp.databinding.FragmentHomeBinding
import com.example.grocerylistapp.ui.adapter.GroceryListsAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: GroceryListsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        setFragmentResultListener("manage_list") {_, _ ->
            viewModel.getGroceryLists()
        }

        lifecycleScope.launch {
            viewModel.groceryLists.collect() {
                adapter.setGroceryLists(it)
            }
        }

        binding.fabAdd.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeToAddList()
            findNavController().navigate(action)
        }
    }

    fun setupAdapter() {
        adapter = GroceryListsAdapter(groceryLists = emptyList(), onClick = { groceryList ->
            val action = HomeFragmentDirections.actionHomeToDetails(groceryList.id!!)
            findNavController().navigate(action)
        })
        binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHome.adapter = adapter
    }
}