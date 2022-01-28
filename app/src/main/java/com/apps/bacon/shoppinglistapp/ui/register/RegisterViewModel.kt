package com.apps.bacon.shoppinglistapp.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class RegisterViewModel : ViewModel() {
    var authResult = MutableLiveData<Any>().apply {
        //init value on start
        value = null
    }

    fun signUpUser(email: String, password: String) = viewModelScope.launch(Dispatchers.Default) {
        authResult.postValue(createUser(email, password))
    }

    private suspend fun createUser(email: String, password: String): Any {
        val auth = FirebaseAuth.getInstance()
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            e.localizedMessage
        }
    }
}