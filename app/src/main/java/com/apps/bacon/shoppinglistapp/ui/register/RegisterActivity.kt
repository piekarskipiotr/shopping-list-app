package com.apps.bacon.shoppinglistapp.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import com.apps.bacon.shoppinglistapp.databinding.ActivityRegisterBinding
import com.apps.bacon.shoppinglistapp.ui.home.HomeActivity
import com.apps.bacon.shoppinglistapp.utils.Button
import com.apps.bacon.shoppinglistapp.utils.EmptyValidation
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var isEmailValid = false
    private var isPasswordValid = false
    private var isConfirmPasswordValid = false
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initValidation()
        binding.signUpButton.setOnClickListener {
            registerViewModel.signUpUser(
                binding.emailTextInput.text.toString(),
                binding.passwordTextInput.text.toString()
            )
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        registerViewModel.authResult.observe(this) {
            if (it is String) {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            } else if (it is AuthResult) {
                registerViewModel.createUserInfo(it.user!!.uid)
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }

        // set button state on view created or recreated
        setAddButtonState(
            EmptyValidation.validate(binding.emailTextInput.text) && EmptyValidation.validate(binding.passwordTextInput.text) && EmptyValidation.validate(binding.confirmPasswordTextInput.text)
        )
    }

    private fun initValidation() {
        binding.emailTextInput.onTextChanged {
            isEmailValid = EmptyValidation.validate(it)
            setAddButtonState(isEmailValid && isPasswordValid && isConfirmPasswordValid)
        }

        binding.passwordTextInput.onTextChanged {
            isPasswordValid = EmptyValidation.validate(it)
            setAddButtonState(isEmailValid && isPasswordValid && isConfirmPasswordValid)
        }

        binding.confirmPasswordTextInput.onTextChanged {
            isConfirmPasswordValid =
                binding.confirmPasswordTextInput.text.toString() == binding.passwordTextInput.text.toString()
            setAddButtonState(isEmailValid && isPasswordValid && isConfirmPasswordValid)
        }
    }

    private fun setAddButtonState(isEnabled: Boolean) {
        if (isEnabled) {
            binding.signUpButton.apply {
                isClickable = true
                alpha = Button.State.Enable.alpha
            }
        } else {
            binding.signUpButton.apply {
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