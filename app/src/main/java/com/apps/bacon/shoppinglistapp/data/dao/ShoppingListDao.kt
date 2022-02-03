package com.apps.bacon.shoppinglistapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.apps.bacon.shoppinglistapp.data.entities.ShoppingList

@Dao
interface ShoppingListDao {
    @Query("SELECT * FROM shopping_list WHERE id = :shoppingListId AND user_id = :userId")
    fun getShoppingListById(shoppingListId: Long, userId: String): ShoppingList

    //in room sql 0 stands for false and unarchived list of shopping lists is on tab at position 0
    @Query("SELECT * FROM shopping_list WHERE is_archived = :selectedTab AND user_id = :userId")
    fun getShoppingListsByArchivedStatus(selectedTab: Int, userId: String): LiveData<List<ShoppingList>>

    @Query("SELECT EXISTS(SELECT * FROM shopping_list WHERE id = :shoppingListId)")
    fun isShoppingListExists(shoppingListId: Long): Boolean

    @Insert
    suspend fun insert(shoppingList: ShoppingList)

    @Delete
    suspend fun delete(shoppingList: ShoppingList)

    @Update
    suspend fun update(shoppingList: ShoppingList)
}