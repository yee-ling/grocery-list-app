package com.example.grocerylistapp.ui.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerylistapp.data.model.GroceryList
import com.example.grocerylistapp.data.model.Item
import com.example.grocerylistapp.data.repo.GroceryListsRepo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditDetailsViewModel(
    private val repo: GroceryListsRepo = GroceryListsRepo.getInstance()
) : ViewModel() {
    private val _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()

    private var _list = MutableStateFlow<GroceryList?>(null)
    val list = _list.asStateFlow()

    private val _pendingItems = MutableStateFlow<List<Item>>(emptyList())
    val pendingItems = _pendingItems.asStateFlow()

    fun getGroceryList(id: Int) {
        repo.getListById(id)?.let { groceryList ->
            _list.value = groceryList
            _pendingItems.value = groceryList.item.toList()
        }
    }

    fun removeItem(item: Item) {
        _pendingItems.value = _pendingItems.value.filter { it.id != item.id }
    }

    fun updateList(name: String, desc: String) {
        val updatedList = _list.value?.copy(
            name = name,
            desc = desc,
            item = _pendingItems.value.toMutableList()
        )
        repo.updateList(_list.value?.id!!, updatedList!!)
        viewModelScope.launch {
            _finish.emit(Unit)
        }
    }
}