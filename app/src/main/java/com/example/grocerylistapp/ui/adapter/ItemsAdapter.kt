package com.example.grocerylistapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.grocerylistapp.data.model.Item
import com.example.grocerylistapp.databinding.LayoutItemItemBinding

class ItemsAdapter(
    private var items: List<Item>
): RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val binding = LayoutItemItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder,
        position: Int
    ) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount() = items.size

    fun setItems(items: List<Item>) {
        this.items = items
        notifyDataSetChanged()
    }


    inner class ItemViewHolder(
        private val binding: LayoutItemItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.run {
                tvItemName.text = item.name
                tvItemQuantity.text = item.quantity.toString()
                tvItemCategory.text = "Category: ${item.category.name}"
            }
        }
    }
}