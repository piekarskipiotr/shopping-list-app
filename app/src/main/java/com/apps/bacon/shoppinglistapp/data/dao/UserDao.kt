package com.apps.bacon.shoppinglistapp.data.dao

import androidx.room.*
import com.apps.bacon.shoppinglistapp.data.entities.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE id = :userId")
    fun getUserById(userId: String): User

    @Insert
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Update
    suspend fun update(user: User)
}