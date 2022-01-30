package com.apps.bacon.shoppinglistapp.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apps.bacon.shoppinglistapp.data.entities.User
import com.apps.bacon.shoppinglistapp.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    var authResult = MutableLiveData<Any>().apply {
        //init value on start
        value = null
    }

    fun createUserInfo(userId: String) = viewModelScope.launch(Dispatchers.Default) {
        val user = User(
            userId,
            Date(),
        )

        userRepository.insert(user)
    }

    fun signUpUser(email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
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