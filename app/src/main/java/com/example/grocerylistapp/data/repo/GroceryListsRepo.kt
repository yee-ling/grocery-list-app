package com.example.grocerylistapp.data.repo

import com.example.grocerylistapp.data.model.GroceryList
import com.example.grocerylistapp.data.model.Item

class GroceryListsRepo private constructor() {
    val map = mutableMapOf<Int, GroceryList>()
    var counter = 0

    init {
        generateRandom(2)
    }

    fun addNewList(list: GroceryList) {
        map[++counter] = list.copy(id = counter)
    }

    fun addItemToList(listId: Int, item: Item): Item? {
        val list = map[listId]?: return null
        val itemsRepo = ItemsRepo.getInstance()
        val newItem = itemsRepo.addItem(item)

        val item = list.item.toMutableList().apply {
            add(newItem)
        }
        map[listId] = list.copy(item = item)
        return newItem
    }

    fun getListById(id:Int): GroceryList? {
        return map[id]
    }

    fun getLists() = map.values.toList()

    fun deleteList(id: Int) {
        map.remove(id)
    }

    fun updateList(id: Int, list: GroceryList) {
        map[id] = list.copy(id)
    }

    fun updateIsChecked(listId: Int, itemId: Int, checked: Boolean) {
        val list = map[listId]?: return
        val updatedItems = list.item.map { item ->
            if(item.id == itemId) item.copy(isChecked = checked)
            else item
        }.toMutableList()
        map[listId] = list.copy(item = updatedItems)
    }

    fun generateRandom(n: Int) {
        val categoriesRepo = CategoriesRepo.getInstance()
        val fruits = categoriesRepo.getCategoryById(1)

        repeat(n) {
            val list = GroceryList(
                name = "Grocery List $it",
                desc = "Short description $it",
                item = mutableListOf(
                    Item(id = 1, name = "Apple", quantity = 2, category = fruits!!),
                    Item(id = 2, name = "Orange", quantity = 3, category = fruits)
                )
            )
            addNewList(list)
        }
    }

    companion object {
        private var instance: GroceryListsRepo? = null

        fun getInstance(): GroceryListsRepo {
            if(instance == null) {
                instance = GroceryListsRepo()
            }
            return instance!!
        }
    }
}