package com.example.grocerylistapp.data.model

data class Category(
    val id: Int? = null,
    val name: String,
) {
    override fun toString(): String = name
}