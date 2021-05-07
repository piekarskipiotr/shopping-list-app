package com.apps.bacon.shoppinglistapp.data.repository

import com.apps.bacon.shoppinglistapp.data.AppDatabase
import com.apps.bacon.shoppinglistapp.data.entities.Grocery
import javax.inject.Inject

class GroceryRepository @Inject constructor(
    private val database: AppDatabase
) {
    fun getGroceryForShoppingList(shoppingListId: Int) = database.groceryDao().getGroceryForShoppingList(shoppingListId)

    fun getNumberOfAllGroceryInShoppingList(shoppingListId: Int) = database.groceryDao().getNumberOfAllGroceryInShoppingList(shoppingListId)

    fun getNumberOfDoneGroceryInShoppingList(shoppingListId: Int) = database.groceryDao().getNumberOfDoneGroceryInShoppingList(shoppingListId)

    suspend fun insert(grocery: Grocery) = database.groceryDao().insert(grocery)

    suspend fun update(grocery: Grocery) = database.groceryDao().update(grocery)

    suspend fun delete(grocery: Grocery) = database.groceryDao().delete(grocery)
}