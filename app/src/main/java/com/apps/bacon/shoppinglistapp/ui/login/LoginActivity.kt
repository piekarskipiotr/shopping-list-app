package com.apps.bacon.shoppinglistapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import com.apps.bacon.shoppinglistapp.databinding.ActivityLoginBinding
import com.apps.bacon.shoppinglistapp.ui.home.HomeActivity
import com.apps.bacon.shoppinglistapp.ui.register.RegisterActivity
import com.apps.bacon.shoppinglistapp.utils.Button
import com.apps.bacon.shoppinglistapp.utils.NameValidation
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var isEmailValid = false
    private var isPasswordValid = false
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initValidation()

        binding.signInButton.setOnClickListener {
            loginViewModel.signInUser(binding.emailTextInput.text.toString(), binding.passwordTextInput.text.toString())
        }

        loginViewModel.authResult.observe(this) {
            if (it is String) {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            } else if (it is AuthResult) {
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }

        binding.signUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // set button state on view created or recreated
        setAddButtonState(
            !binding.emailTextInput.text.isNullOrEmpty() && !binding.passwordTextInput.text.isNullOrEmpty()
        )
    }

    private fun initValidation() {
        binding.emailTextInput.onTextChanged {
            isEmailValid = !it.isNullOrEmpty()
            setAddButtonState(isEmailValid && isPasswordValid)
        }

        binding.passwordTextInput.onTextChanged {
            isPasswordValid = !it.isNullOrEmpty()
            setAddButtonState(isEmailValid && isPasswordValid)
        }
    }

    private fun setAddButtonState(isEnabled: Boolean) {
        if (isEnabled) {
            binding.signInButton.apply {
                isClickable = true
                alpha = Button.State.Enable.alpha
            }
        } else {
            binding.signInButton.apply {
                isClickable = false
                alpha = Button.State.Disable.alpha
            }
        }
    }

    private fun TextInputEditText.onTextChanged(onTextChanged: (CharSequence?) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                onTextChanged.invoke(p0)
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }
}