package com.example.grocerylistapp.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerylistapp.data.model.GroceryList
import com.example.grocerylistapp.data.repo.GroceryListsRepo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repo: GroceryListsRepo = GroceryListsRepo.getInstance()
) : ViewModel() {

    private var _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()

    private var _list = MutableStateFlow(GroceryList(
            name = "",
            desc = "",
            item = mutableListOf()
        )
    )
    val list = _list.asStateFlow()

    fun getListById(id: Int) {
        viewModelScope.launch {
            repo.getListById(id)?.let {
                _list.value = it
            }
        }
    }

    fun deleteGroceryList(id: Int) {
        repo.deleteList(id)
        viewModelScope.launch {
            _finish.emit(Unit)
        }
    }

    fun updateIsChecked(listId: Int, itemId: Int, checked: Boolean) {
        viewModelScope.launch {
            repo.updateIsChecked(listId = listId, itemId = itemId, checked = checked)
            repo.getListById(listId)?.let {
                _list.value = it
            }
        }
    }
}
















