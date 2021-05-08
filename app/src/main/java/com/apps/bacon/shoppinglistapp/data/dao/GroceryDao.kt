package com.apps.bacon.shoppinglistapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.apps.bacon.shoppinglistapp.data.entities.Grocery

@Dao
interface GroceryDao {
    @Query("SELECT * FROM grocery WHERE shopping_list_id = :shoppingListId ORDER BY is_done = 1")
    fun getGroceryForShoppingList(shoppingListId: Int): LiveData<List<Grocery>>

    @Insert
    suspend fun insert(grocery: Grocery)

    @Update
    suspend fun update(grocery: Grocery)
}