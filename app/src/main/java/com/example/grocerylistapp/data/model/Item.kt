package com.example.grocerylistapp.data.model

data class Item(
    val id: Int? = null,
    val name: String,
    val quantity: Int = 1,
    val isChecked: Boolean = false,
    val category: Category
)