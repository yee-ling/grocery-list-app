package com.example.grocerylistapp.data.repo

import com.example.grocerylistapp.data.model.Item


class ItemsRepo private constructor() {
    val map = mutableMapOf<Int, Item>()
    var counter = 0

//    fun addItem(item: Item) {
//        map[++counter] = item.copy(id=counter)
//    }

    fun addItem(item: Item): Item {
        val newItem = item.copy(id = ++counter)
        map[newItem.id!!] = newItem
        return newItem
    }

    fun getItemById(id:Int): Item? {
        return map[id]
    }

    fun getItems() = map.values.toList()

    fun deleteItem(id: Int) {
        map.remove(id)
    }

    fun updateItem(id: Int, item: Item) {
        map[id] = item
    }

    companion object {
        private var instance: ItemsRepo? = null

        fun getInstance(): ItemsRepo {
            if(instance == null) {
                instance = ItemsRepo()
            }
            return instance!!
        }
    }
}