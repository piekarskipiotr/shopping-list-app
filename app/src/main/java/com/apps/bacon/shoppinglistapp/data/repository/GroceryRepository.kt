package com.apps.bacon.shoppinglistapp.data.repository

import com.apps.bacon.shoppinglistapp.data.AppDatabase
import com.apps.bacon.shoppinglistapp.data.entities.Grocery
import javax.inject.Inject

class GroceryRepository @Inject constructor(
    private val database: AppDatabase
) {
    fun getGroceryForShoppingList(shoppingListId: Int, userId: String) = database.groceryDao().getGroceryForShoppingList(shoppingListId, userId)

    fun getAllGroceryForUser(userId: String) = database.groceryDao().getAllForUser(userId)

    suspend fun deleteGroceryFromDeletedShoppingList(shoppingListId: Int, userId: String) = database.groceryDao().deleteGroceryFromDeletedShoppingList(shoppingListId, userId)

    suspend fun insert(grocery: Grocery) = database.groceryDao().insert(grocery)

    suspend fun delete(grocery: Grocery) = database.groceryDao().delete(grocery)

    suspend fun update(grocery: Grocery) = database.groceryDao().update(grocery)

    suspend fun insertOrUpdate(grocery: Grocery) {
        if (database.groceryDao().isGroceryExists(grocery.id))
            database.groceryDao().update(grocery)
        else
            database.groceryDao().insert(grocery)
    }
}