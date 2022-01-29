package com.apps.bacon.shoppinglistapp.data.repository

import com.apps.bacon.shoppinglistapp.data.AppDatabase
import com.apps.bacon.shoppinglistapp.data.entities.ShoppingList
import javax.inject.Inject

class ShoppingListRepository @Inject constructor(
    private val database: AppDatabase
) {
    fun getShoppingListById(shoppingListId: Int, userId: String) =
        database.shoppingListDao().getShoppingListById(shoppingListId, userId)

    fun getShoppingListsByArchivedStatus(selectedTab: Int, userId: String) =
        database.shoppingListDao().getShoppingListsByArchivedStatus(selectedTab, userId)

    suspend fun insert(shoppingList: ShoppingList) = database.shoppingListDao().insert(shoppingList)

    suspend fun delete(shoppingList: ShoppingList) = database.shoppingListDao().delete(shoppingList)

    suspend fun update(shoppingList: ShoppingList) = database.shoppingListDao().update(shoppingList)
}