package com.apps.bacon.shoppinglistapp.ui.login
import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.bacon.shoppinglistapp.data.entities.Grocery
import com.apps.bacon.shoppinglistapp.data.entities.ShoppingList
import com.apps.bacon.shoppinglistapp.data.entities.User
import com.apps.bacon.shoppinglistapp.data.repository.GroceryRepository
import com.apps.bacon.shoppinglistapp.data.repository.ShoppingListRepository
import com.apps.bacon.shoppinglistapp.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val shoppingListRepository: ShoppingListRepository,
    private val groceryRepository: GroceryRepository,
    private val database: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {
    var authResult = MutableLiveData<Any>().apply {
        //init value on start
        value = null
    }

    var signingIn = MutableLiveData<Boolean>().apply {
        value = false
    }

    fun checkNeedToUpdate(userId: String) = viewModelScope.launch(Dispatchers.IO) {
        val user = database.collection("users").document(userId).get().await().toObject(User::class.java)
        var localUser = userRepository.getUserById(userId)
        if (localUser == null) {
            localUser = User(
                userId,
                Date(),
            )

            userRepository.insert(localUser)
            updateLocalDatabase(userId)
        } else {
            if (user!!.lastUpdate.after(localUser.lastUpdate)) {
                updateLocalDatabase(userId)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun updateLocalDatabase(userId: String) {
        val shoppingList = FirebaseFirestore.getInstance().collection("shoppinglists").document(userId).get().await().data
        val groceries = FirebaseFirestore.getInstance().collection("grocery").document(userId).get().await().data

        if (shoppingList != null) {
            for (e in shoppingList) {
                val sl = Mapper.mapShoppingList(e.value as Map<String, Any>)
                shoppingListRepository.insertOrUpdate(sl)
            }
        }

        if (groceries != null) {
            for (e in groceries) {
                val g = Mapper.mapGrocery(e.value as Map<String, Any>)
                groceryRepository.insertOrUpdate(g)
            }
        }
    }

    fun signInUser(email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        signingIn.postValue(true)
        authResult.postValue(signIn(email, password))
        signingIn.postValue(false)
    }

    private suspend fun signIn(email: String, password: String): Any {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            e.localizedMessage
        }
    }

    private object Mapper{
        @SuppressLint("SimpleDateFormat")
        fun mapShoppingList(map: Map<String, Any>): ShoppingList {
            return ShoppingList(
                map["id"]!!.toString().toLong(),
                map["name"]!!.toString(),
                map["doneGroceries"]!!.toString().toInt(),
                map["allGroceries"]!!.toString().toInt(),
                map["archived"]!!.toString().toBoolean(),
                map["userId"]!!.toString(),
            )
        }

        fun mapGrocery(map: Map<String, Any>): Grocery {
            return Grocery(
                map["id"]!!.toString().toLong(),
                map["name"]!!.toString(),
                map["pieces"]!!.toString().toInt(),
                map["done"]!!.toString().toBoolean(),
                map["shoppingListId"]!!.toString().toLong(),
                map["userId"]!!.toString(),
            )
        }
    }
}