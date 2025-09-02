package com.example.grocerylistapp.ui.item

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.grocerylistapp.R
import com.example.grocerylistapp.data.model.Category
import com.example.grocerylistapp.databinding.FragmentAddItemBinding
import com.example.grocerylistapp.ui.category.AddCategoryViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import kotlin.getValue

class AddItemFragment : Fragment() {
    private val viewModel: AddItemViewModel by viewModels()
    private val categoryViewModel: AddCategoryViewModel by viewModels()
    private lateinit var binding: FragmentAddItemBinding
    private lateinit var selectedCategory: Category
    private val args: AddItemFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listId = args.listId

        lifecycleScope.launch {
            viewModel.finish.collect { newItem ->
                setFragmentResult("new_item", Bundle().apply {
                    putString("name", newItem.name)
                    putInt("quantity", newItem.quantity)
                    putString("category", newItem.category.name)
                })
                findNavController().popBackStack()
            }
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

        setFragmentResultListener("manage_category") {_, _ ->
            categoryViewModel.getCategories()
        }

        lifecycleScope.launch {
            categoryViewModel.categories.collect { categories ->
                val categoriesList = categories + Category(-1, "Add New Category")
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,categoriesList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.categoriesSpinner.adapter=adapter
                binding.categoriesSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val selected = categoriesList[position]

                        if(selected.id == -1) {
                            val action = AddItemFragmentDirections.actionAddItemToAddCategory()
                            findNavController().navigate(action)
                            binding.categoriesSpinner.setSelection(0)
                        } else {
                            selectedCategory = selected
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
            }
        }

        binding.run {
            fabSubmitItem.setOnClickListener {
                val name = etItemName.text.toString()
                val quantity = etItemQuantity.text.toString()
                val category = selectedCategory

                if(listId == -1) {
                    viewModel.addItem(name, quantity, category)
                } else {
                    viewModel.addItemToList(listId, name, quantity, category)
                }
            }
        }
    }
}