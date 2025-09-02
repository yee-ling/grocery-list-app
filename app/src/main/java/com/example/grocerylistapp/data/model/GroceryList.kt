package com.example.grocerylistapp.data.model

data class GroceryList(
    val id: Int? = null,
    val name: String,
    val desc: String,
    val item: MutableList<Item>
)