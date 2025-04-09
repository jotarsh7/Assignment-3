package com.example.favoritemovieapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.example.favoritemovieapp.databinding.ActivityRegisterBinding
import com.example.favoritemovieapp.viewmodel.AuthViewModel

/**
 * RegisterActivity provides the user interface for new user registration.
 * It allows users to enter their email and password, displays password length errors,
 * and initiates the registration process via AuthViewModel. If registration is successful,
 * it navigates the user back to the LoginActivity.
 */
class RegisterActivity : AppCompatActivity() {

    // View binding instance to access views defined in ActivityRegister layout.
    private lateinit var binding: ActivityRegisterBinding

    // Obtain an instance of AuthViewModel to handle registration and authentication logic.
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using view binding and set it as the content view.
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listener for the registration button.
        binding.buttonRegister.setOnClickListener {
            // Get the email and password inputs from the user.
            val email = binding.editTextRegisterEmail.text.toString().trim()
            val password = binding.editTextRegisterPassword.text.toString()

            // Check if the entered password meets the minimum length requirement.
            if (password.length < 6) {
                // If password is too short, display an error message.
                binding.errorText.text = "Password must be at least 6 characters."
                binding.errorText.visibility = View.VISIBLE
            } else {
                // If password meets the requirement, hide any previous error messages.
                binding.errorText.visibility = View.GONE
                // Call the registration method in the AuthViewModel to register the user.
                authViewModel.register(email, password)
            }
        }

        // Set click listener for the "Cancel" button to close the registration activity.
        binding.buttonRegisterCancel.setOnClickListener {
            finish()
        }

        // Observe the authentication result from the AuthViewModel.
        // Upon successful registration, navigate back to LoginActivity.
        authViewModel.authResult.observe(this) { (success, _) ->
            if (success) {
                // Start the LoginActivity if registration is successful.
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }
}
