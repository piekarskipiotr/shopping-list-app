package com.apps.bacon.shoppinglistapp.data.repository

import com.apps.bacon.shoppinglistapp.data.AppDatabase
import com.apps.bacon.shoppinglistapp.data.FirebaseDatabase
import com.apps.bacon.shoppinglistapp.data.entities.ShoppingList
import com.apps.bacon.shoppinglistapp.data.entities.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val database: AppDatabase,
    private val firebaseDatabase: FirebaseDatabase
) {
    fun getUserById(userId: String) =
        database.userDao().getUserById(userId)

    suspend fun insert(user: User) {
        database.userDao().insert(user)
        firebaseDatabase.insert(user)
    }
}