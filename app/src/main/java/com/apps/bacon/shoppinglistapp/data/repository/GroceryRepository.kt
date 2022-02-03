package com.apps.bacon.shoppinglistapp.data.repository

import com.apps.bacon.shoppinglistapp.data.AppDatabase
import com.apps.bacon.shoppinglistapp.data.FirebaseDatabase
import com.apps.bacon.shoppinglistapp.data.entities.Grocery
import com.apps.bacon.shoppinglistapp.data.entities.ShoppingList
import java.util.*
import javax.inject.Inject

class GroceryRepository @Inject constructor(
    private val database: AppDatabase,
    private val firebaseDatabase: FirebaseDatabase
) {
    fun getGroceryForShoppingList(shoppingListId: Long, userId: String) =
        database.groceryDao().getGroceryForShoppingList(shoppingListId, userId)

    suspend fun deleteGroceryFromDeletedShoppingList(shoppingListId: Long, userId: String) {
        firebaseDatabase.deleteGroceryFromDeletedShoppingList(
            database.groceryDao().getGroceryOfDeletedShoppingList(shoppingListId, userId)
        )
        database.groceryDao().deleteGroceryFromDeletedShoppingList(shoppingListId, userId)
        updateUserLastModificationDate(userId)
    }

    suspend fun insert(grocery: Grocery) {
        database.groceryDao().insert(grocery)
        firebaseDatabase.insert(grocery)
        updateUserLastModificationDate(grocery.userId)
    }

    suspend fun delete(grocery: Grocery) {
        database.groceryDao().delete(grocery)
        firebaseDatabase.delete(grocery)
        updateUserLastModificationDate(grocery.userId)
    }

    suspend fun update(grocery: Grocery) {
        database.groceryDao().update(grocery)
        firebaseDatabase.update(grocery)
        updateUserLastModificationDate(grocery.userId)
    }

    suspend fun insertOrUpdate(grocery: Grocery) {
        if (database.groceryDao().isGroceryExists(grocery.id))
            database.groceryDao().update(grocery)
        else
            database.groceryDao().insert(grocery)
    }

    suspend fun mergeFirebaseToLocal(firebaseList: List<Grocery>, userId: String) {
        for (grocery in firebaseList) {
            if (database.groceryDao().isGroceryExists(grocery.id))
                database.groceryDao().update(grocery)
            else
                database.groceryDao().insert(grocery)
        }

        // delete local grocery that was deleted from other device or from server
        val list = database.groceryDao().getGroceriesAllForUser(userId)
        for (shoppingList in list)
            if (!firebaseList.contains(shoppingList))
                database.groceryDao().delete(shoppingList)
    }

    private suspend fun updateUserLastModificationDate(userId: String) {
        val user = database.userDao().getUserById(userId)!!
        user.apply {
            lastUpdate = Date()
        }

        database.userDao().update(user)
        firebaseDatabase.update(user)
    }
}