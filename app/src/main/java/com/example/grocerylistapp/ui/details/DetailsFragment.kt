package com.example.grocerylistapp.ui.details

import android.annotation.SuppressLint
import android.graphics.Paint
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.grocerylistapp.R
import com.example.grocerylistapp.databinding.CategoryHeaderBinding
import com.example.grocerylistapp.databinding.FragmentDetailsBinding
import com.example.grocerylistapp.databinding.ItemCheckboxBinding
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private val args: DetailsFragmentArgs by navArgs()
    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getListById(args.listId)

        lifecycleScope.launch {
            viewModel.list.collect {
                binding.run {
                    tvListName.text = it.name
                    tvListDesc.text = it.desc

                    llItemsContainer.removeAllViews()

                    val grouped = it.item.groupBy { item -> item.category.name }

                    grouped.forEach { (categoryName, items) ->
                        val headerBinding = CategoryHeaderBinding.inflate(layoutInflater, llItemsContainer, false)
                        headerBinding.tvCategoryHeader.text = categoryName
                        llItemsContainer.addView(headerBinding.root)

                        items.forEach { item ->
                            val itemBinding = ItemCheckboxBinding.inflate(layoutInflater, llItemsContainer, false)
                            itemBinding.tvItem.text = item.name
                            itemBinding.tvQuantity.text = "Quantity: ${item.quantity}"

                            itemBinding.cbItem.isChecked = item.isChecked
                            itemBinding.tvItem.paintFlags = if(item.isChecked) {
                                itemBinding.tvItem.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                            } else {
                                itemBinding.tvItem.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                            }

                            itemBinding.cbItem.setOnCheckedChangeListener { _, isChecked ->
                                viewModel.updateIsChecked(args.listId, item.id!!, isChecked)
                            }
                            llItemsContainer.addView(itemBinding.root)
                        }
                    }
                }
            }
        }

        binding.mbDeleteList.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Are you sure?")
                .setMessage("Do you want to delete this list? Action cannot be undone.")
                .setNegativeButton("Cancel") {dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Delete") {dialog, _ ->
                    viewModel.deleteGroceryList(args.listId)
                    dialog.dismiss()
                }
                .show()
        }

        binding.mbUpdateList.setOnClickListener {
            val action = DetailsFragmentDirections.actionDetailsToEditDetails(args.listId)
            findNavController().navigate(action)
        }

        lifecycleScope.launch {
            viewModel.finish.collect {
                setFragmentResult("manage_list", Bundle())
                findNavController().popBackStack(R.id.homeFragment, false)
            }
        }
    }
}