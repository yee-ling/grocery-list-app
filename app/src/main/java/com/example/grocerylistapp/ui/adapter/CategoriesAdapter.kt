package com.example.grocerylistapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.grocerylistapp.data.model.Category
import com.example.grocerylistapp.databinding.LayoutItemCategoryBinding

class CategoriesAdapter(
    private var categories: List<Category>
): RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = LayoutItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
    }

    inner class CategoryViewHolder(
        private val binding: LayoutItemCategoryBinding
    ):RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.run{
                tvCategoryName.text = category.name
            }
        }
    }
}