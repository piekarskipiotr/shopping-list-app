package com.apps.bacon.shoppinglistapp.data

import com.apps.bacon.shoppinglistapp.data.entities.Grocery
import com.apps.bacon.shoppinglistapp.data.entities.ShoppingList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDatabase @Inject constructor(
    private val database: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    suspend fun insert(shoppingList: ShoppingList) {
        val userId = auth.currentUser!!.uid
        val shoppingListMap: MutableMap<String, ShoppingList> = mutableMapOf()
        shoppingListMap[shoppingList.id.toString()] = shoppingList
        database.collection("shoppinglists").document(userId).set(shoppingListMap, SetOptions.merge()).await()
    }

    suspend fun insert(grocery: Grocery) {
        val userId = auth.currentUser!!.uid
        val groceryMap: MutableMap<String, Grocery> = mutableMapOf()
        groceryMap[grocery.id.toString()] = grocery
        database.collection("grocery").document(userId).set(groceryMap, SetOptions.merge()).await()
    }

    suspend fun delete(shoppingList: ShoppingList) {
        val userId = auth.currentUser!!.uid
        val doc = database.collection("shoppinglists").document(userId)
        val update: MutableMap<String, Any> = hashMapOf(
            "${shoppingList.id}" to FieldValue.delete()
        )

        doc.update(update).await()
    }

    suspend fun delete(grocery: Grocery) {
        val userId = auth.currentUser!!.uid
        val doc = database.collection("grocery").document(userId)
        val update: MutableMap<String, Any> = hashMapOf(
            "${grocery.id}" to FieldValue.delete()
        )

        doc.update(update).await()
    }

    suspend fun deleteGroceryFromDeletedShoppingList(groceries: List<Grocery>) {
        val userId = auth.currentUser!!.uid
        val doc = database.collection("grocery").document(userId)

        val update: MutableMap<String, Any> = hashMapOf()
        for (grocery in groceries)
            update[grocery.id.toString()] = FieldValue.delete()

        doc.update(update).await()
    }

    suspend fun update(shoppingList: ShoppingList) {
        val userId = auth.currentUser!!.uid
        val doc = database.collection("shoppinglists").document(userId)
        doc.update("${shoppingList.id}", shoppingList).await()
    }

    suspend fun update(grocery: Grocery) {
        val userId = auth.currentUser!!.uid
        val doc = database.collection("grocery").document(userId)
        doc.update("${grocery.id}", grocery).await()
    }
}