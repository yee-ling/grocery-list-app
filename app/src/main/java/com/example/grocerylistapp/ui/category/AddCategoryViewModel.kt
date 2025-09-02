package com.example.grocerylistapp.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerylistapp.data.model.Category
import com.example.grocerylistapp.data.repo.CategoriesRepo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddCategoryViewModel(
    private val repo: CategoriesRepo = CategoriesRepo.getInstance()
) : ViewModel() {

    private val _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

    private var _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    init {
        getCategories()
    }

    fun getCategories() {
        _categories.value = repo.getCategories()
    }

    fun addCategory(category: Category) {
        try {
            require(category.name.isNotBlank()) {"Category name cannot be blank"}
            repo.addNewCategory(category)
            getCategories()
            viewModelScope.launch {
                _finish.emit(Unit)
            }
        } catch (e: Exception) {
            viewModelScope.launch {
                _error.emit(e.message.toString())
            }
        }
    }
}