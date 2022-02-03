package com.apps.bacon.shoppinglistapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.apps.bacon.shoppinglistapp.ui.home.HomeActivity
import com.apps.bacon.shoppinglistapp.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentUser = auth.currentUser

        intent = if (currentUser != null)
            Intent(this, HomeActivity::class.java)
        else
            Intent(this, LoginActivity::class.java)

        startActivity(intent)
        finish()
    }
}