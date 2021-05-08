package com.apps.bacon.shoppinglistapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.apps.bacon.shoppinglistapp.data.entities.ShoppingList

@Dao
interface ShoppingListDao {
    @Query("SELECT * FROM shopping_list WHERE id = :shoppingListId")
    fun getShoppingListById(shoppingListId: Int): ShoppingList

    @Query("SELECT * FROM shopping_list WHERE is_archived = 1 ORDER BY date DESC")
    fun getArchivedShoppingLists(): LiveData<List<ShoppingList>>

    @Insert
    suspend fun insert(shoppingList: ShoppingList)

    @Update
    suspend fun update(shoppingList: ShoppingList)

    @Delete
    suspend fun delete(shoppingList: ShoppingList)
}