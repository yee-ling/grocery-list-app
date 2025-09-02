package com.example.grocerylistapp.ui.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerylistapp.data.model.Category
import com.example.grocerylistapp.data.model.Item
import com.example.grocerylistapp.data.repo.GroceryListsRepo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AddItemViewModel(
    private val listsRepo: GroceryListsRepo = GroceryListsRepo.getInstance()
) : ViewModel() {
    private val _finish = MutableSharedFlow<Item>()
    val finish = _finish.asSharedFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    fun addItem(name: String, quantityStr: String, category: Category) {
        try {
            require(name.isNotBlank()) {"Item name cannot be blank"}

            val quantity = quantityStr.toIntOrNull()
            require(quantity != null && quantity > 0) {"Please enter a valid amount"}
            require(category.name.isNotBlank()) {"Please enter a category"}
            val item = Item(name = name, quantity = quantity, category = category)

            viewModelScope.launch {
                _finish.emit(item)
            }
        } catch (e: Exception) {
            viewModelScope.launch { _error.emit(e.message.toString()) }
        }
    }

    fun addItemToList(listId: Int, name: String, quantityStr: String, category: Category) {
        require(name.isNotBlank()) {"Item name cannot be blank"}
        val quantity = quantityStr.toIntOrNull()
        require(quantity != null && quantity > 0) {"Please enter a valid amount"}
        require(category.name.isNotBlank()) {"Please enter a category"}

        val item = Item(name = name, quantity = quantity, category = category)

        val savedItem = listsRepo.addItemToList(listId, item)

        viewModelScope.launch { _finish.emit(savedItem!!) }
    }
}