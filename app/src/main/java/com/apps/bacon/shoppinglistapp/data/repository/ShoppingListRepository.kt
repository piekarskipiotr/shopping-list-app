package com.apps.bacon.shoppinglistapp.data.repository

import com.apps.bacon.shoppinglistapp.data.AppDatabase
import com.apps.bacon.shoppinglistapp.data.FirebaseDatabase
import com.apps.bacon.shoppinglistapp.data.entities.ShoppingList
import java.util.*
import javax.inject.Inject

class ShoppingListRepository @Inject constructor(
    private val database: AppDatabase,
    private val firebaseDatabase: FirebaseDatabase
) {
    fun getShoppingListById(shoppingListId: Long, userId: String) =
        database.shoppingListDao().getShoppingListById(shoppingListId, userId)

    fun getShoppingListsByArchivedStatus(selectedTab: Int, userId: String) =
        database.shoppingListDao().getShoppingListsByArchivedStatus(selectedTab, userId)

    suspend fun insert(shoppingList: ShoppingList) {
        database.shoppingListDao().insert(shoppingList)
        firebaseDatabase.insert(shoppingList)
        updateUserLastModificationDate(shoppingList.userId)
    }

    suspend fun delete(shoppingList: ShoppingList) {
        database.shoppingListDao().delete(shoppingList)
        firebaseDatabase.delete(shoppingList)
        updateUserLastModificationDate(shoppingList.userId)
    }

    suspend fun update(shoppingList: ShoppingList) {
        database.shoppingListDao().update(shoppingList)
        firebaseDatabase.update(shoppingList)
        updateUserLastModificationDate(shoppingList.userId)
    }

    suspend fun mergeFirebaseToLocal(firebaseList: List<ShoppingList>, userId: String) {
        for (shoppingList in firebaseList) {
            if (database.shoppingListDao().isShoppingListExists(shoppingList.id))
                database.shoppingListDao().update(shoppingList)
            else
                database.shoppingListDao().insert(shoppingList)
        }

        // delete local shopping list that was deleted from other device or from server
        val list = database.shoppingListDao().getAllShoppingListForUser(userId)
        for (shoppingList in list)
            if (!firebaseList.contains(shoppingList))
                database.shoppingListDao().delete(shoppingList)
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