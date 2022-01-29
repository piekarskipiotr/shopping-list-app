package com.apps.bacon.shoppinglistapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.apps.bacon.shoppinglistapp.data.entities.Grocery

@Dao
interface GroceryDao {
    @Query("SELECT * FROM grocery WHERE shopping_list_id = :shoppingListId AND user_id = :userId ORDER BY is_done = 1")
    fun getGroceryForShoppingList(shoppingListId: Int, userId: String): LiveData<List<Grocery>>

    @Query("SELECT * FROM grocery WHERE shopping_list_id = :shoppingListId AND user_id = :userId")
    fun getGroceryOfDeletedShoppingList(shoppingListId: Int, userId: String): List<Grocery>

    @Transaction
    suspend fun deleteGroceryFromDeletedShoppingList(shoppingListId: Int, userId: String) {
        val list = getGroceryOfDeletedShoppingList(shoppingListId, userId)
        for (grocery in list) {
            delete(grocery)
        }
    }

    @Insert
    suspend fun insert(grocery: Grocery)

    @Delete
    suspend fun delete(grocery: Grocery)

    @Update
    suspend fun update(grocery: Grocery)
}