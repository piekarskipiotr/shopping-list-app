package com.apps.bacon.shoppinglistapp.data.repository

import com.apps.bacon.shoppinglistapp.data.AppDatabase
import com.apps.bacon.shoppinglistapp.data.entities.ShoppingList
import com.apps.bacon.shoppinglistapp.data.entities.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val database: AppDatabase
) {
    fun getUserById(userId: String) =
        database.userDao().getUserById( userId)

    suspend fun insert(user: User) {
        FirebaseFirestore.getInstance().collection("users").document(user.id).set(user).await()
        database.userDao().insert(user)
    }

    suspend fun delete(user: User) = database.userDao().delete(user)

    suspend fun update(user: User) = database.userDao().update(user)

    suspend fun updateSyncDate(userId: String) {
        val user = getUserById(userId)!!
        user.apply {
            lastUpdate = Date()
        }

        database.userDao().update(user)
    }
}