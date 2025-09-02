package com.example.grocerylistapp.data.repo

import com.example.grocerylistapp.data.model.Category

class CategoriesRepo private constructor() {
    val map = mutableMapOf<Int, Category>()
    var counter = 0

    init {
        addNewCategory(Category(name = "Fruits"))
        addNewCategory(Category(name = "Vegetables"))
        addNewCategory(Category(name = "Fish"))
        addNewCategory(Category(name = "Snacks"))
    }

    fun addNewCategory(category: Category) {
        map[++counter] = category.copy(id = counter)
    }

    fun getCategoryById(id:Int): Category? {
        return map[id]
    }

    fun getCategories() = map.values.toList()

    fun deleteCategory(id: Int) {
        map.remove(id)
    }

    fun updateCategory(id: Int, category: Category) {
        map[id] = category
    }

    companion object {
        private var instance: CategoriesRepo? = null

        fun getInstance(): CategoriesRepo {
            if(instance == null) {
                instance = CategoriesRepo()
            }
            return instance!!
        }
    }
}