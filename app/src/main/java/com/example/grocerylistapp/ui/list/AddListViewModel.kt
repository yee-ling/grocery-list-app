package com.example.grocerylistapp.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerylistapp.data.model.Category
import com.example.grocerylistapp.data.model.GroceryList
import com.example.grocerylistapp.data.model.Item
import com.example.grocerylistapp.data.repo.GroceryListsRepo
import com.example.grocerylistapp.data.repo.ItemsRepo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AddListViewModel(
    private val repo: GroceryListsRepo = GroceryListsRepo.getInstance(),
    private val itemsRepo: ItemsRepo = ItemsRepo.getInstance()
) : ViewModel() {

    private val _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    val pendingItems = mutableListOf<Item>()

    fun addItem(name: String, quantity: Int, category: Category) {
        val item = Item(name = name, quantity = quantity, category = category)
        pendingItems.add(item)
    }

    fun saveList(name: String, desc: String) {
        try {
            require(name.isNotBlank()) {"List name cannot be blank"}
            require(desc.isNotBlank()) {"List description cannot be blank"}
            require(pendingItems.isNotEmpty()) {"Please add at least 1 item"}

            // Persist items first
            val savedItems = pendingItems.map { itemsRepo.addItem(it) } // save in repo

            // Persist the list with items
            val list = GroceryList(
                name = name,
                desc = desc,
                item = savedItems.toMutableList()
            )
            repo.addNewList(list)
            // reset
            pendingItems.clear()
            viewModelScope.launch { _finish.emit(Unit) }

        } catch (e: Exception) {
            viewModelScope.launch { _error.emit(e.message.toString()) }
        }
    }

}