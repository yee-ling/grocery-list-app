package com.example.grocerylistapp.ui.home

import androidx.lifecycle.ViewModel
import com.example.grocerylistapp.data.model.GroceryList
import com.example.grocerylistapp.data.repo.GroceryListsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(
    private val repo: GroceryListsRepo = GroceryListsRepo.getInstance()
) : ViewModel() {
    private val _groceryLists = MutableStateFlow<List<GroceryList>>(emptyList())
    val groceryLists = _groceryLists.asStateFlow()

    init {
        getGroceryLists()
    }

    fun getGroceryLists() {
        _groceryLists.value = repo.getLists()
    }

}