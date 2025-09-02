package com.example.grocerylistapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.grocerylistapp.data.model.GroceryList
import com.example.grocerylistapp.databinding.LayoutItemGrocerylistBinding

class GroceryListsAdapter(
    private var groceryLists: List<GroceryList>,
    private val onClick: (GroceryList) -> Unit
): RecyclerView.Adapter<GroceryListsAdapter.GroceryListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryListViewHolder {
        val binding = LayoutItemGrocerylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroceryListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: GroceryListViewHolder,
        position: Int
    ) {
        val list = groceryLists[position]
        holder.bind(list)
    }

    override fun getItemCount() = groceryLists.size

    fun setGroceryLists(groceryLists: List<GroceryList>) {
        this.groceryLists = groceryLists
        notifyDataSetChanged()
    }

    inner class GroceryListViewHolder(
        private val binding: LayoutItemGrocerylistBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(groceryList: GroceryList) {
            binding.run {
                tvListName.text = groceryList.name
                tvListDesc.text = groceryList.desc

                llGroceryList.setOnClickListener {
                    onClick(groceryList)
                }
            }
        }
    }
}