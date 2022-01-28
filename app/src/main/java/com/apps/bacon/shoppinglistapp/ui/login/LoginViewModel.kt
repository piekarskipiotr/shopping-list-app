package com.apps.bacon.shoppinglistapp.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class LoginViewModel : ViewModel() {
    var authResult = MutableLiveData<Any>().apply {
        //init value on start
        value = null
    }

    fun signInUser(email: String, password: String) = viewModelScope.launch(Dispatchers.Default) {
        authResult.postValue(signIn(email, password))
    }

    private suspend fun signIn(email: String, password: String): Any {
        val auth = FirebaseAuth.getInstance()
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            e.localizedMessage
        }
    }
}