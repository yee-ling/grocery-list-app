package com.example.grocerylistapp.ui.edit

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.grocerylistapp.R
import com.example.grocerylistapp.databinding.CategoryHeaderBinding
import com.example.grocerylistapp.databinding.FragmentEditDetailsBinding
import com.example.grocerylistapp.databinding.ItemCheckboxBinding
import kotlinx.coroutines.launch

class EditDetailsFragment : Fragment() {
    private lateinit var binding: FragmentEditDetailsBinding
    private val viewModel: EditDetailsViewModel by viewModels()
    private val args: EditDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getGroceryList(args.listId)

        lifecycleScope.launch {
            viewModel.list.collect {
                binding.etListName.setText(it?.name)
                binding.etListDesc.setText(it?.desc)
            }
        }

        lifecycleScope.launch {
            viewModel.pendingItems.collect {
                binding.run {
                    llEditItemsContainer.removeAllViews()

                    val grouped = it.groupBy { item -> item.category.name }

                    grouped.forEach { (categoryName, items) ->
                        val headerBinding = CategoryHeaderBinding.inflate(layoutInflater, llEditItemsContainer, false)
                        headerBinding.tvCategoryHeader.text = categoryName
                        llEditItemsContainer.addView(headerBinding.root)

                        items.forEach { item ->
                            val itemBinding = ItemCheckboxBinding.inflate(layoutInflater, llEditItemsContainer, false)

                            itemBinding.tvItem.text = item.name
                            itemBinding.tvQuantity.text = item.quantity.toString()
                            itemBinding.ivDelete.visibility = View.VISIBLE
                            itemBinding.cbItem.isEnabled = false

                            itemBinding.ivDelete.setOnClickListener {
                                viewModel.removeItem(item)
                            }

                            llEditItemsContainer.addView(itemBinding.root)
                        }
                    }
                }
            }
        }

        binding.mbAddMoreItem.setOnClickListener {
            val action = EditDetailsFragmentDirections.actionEditDetailsToAddItem(args.listId)
            findNavController().navigate(action)
        }

        binding.mbSubmit.setOnClickListener {
            val name = binding.etListName.text.toString()
            val desc = binding.etListDesc.text.toString()
            viewModel.updateList(name, desc)
        }

        lifecycleScope.launch {
            viewModel.finish.collect {
                findNavController().popBackStack(R.id.detailsFragment, false)
            }
        }
    }
}