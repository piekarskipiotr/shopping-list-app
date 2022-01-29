package com.apps.bacon.shoppinglistapp.data.repository

import com.apps.bacon.shoppinglistapp.data.AppDatabase
import com.apps.bacon.shoppinglistapp.data.entities.ShoppingList
import com.apps.bacon.shoppinglistapp.data.entities.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val database: AppDatabase
) {
    fun getUserById(userId: String) =
        database.userDao().getUserById( userId)

    suspend fun insert(user: User) = database.userDao().insert(user)

    suspend fun delete(user: User) = database.userDao().delete(user)

    suspend fun update(user: User) = database.userDao().update(user)
}