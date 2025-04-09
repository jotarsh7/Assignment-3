package com.example.favoritemovieapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.example.favoritemovieapp.databinding.ActivityLoginBinding
import com.example.favoritemovieapp.viewmodel.AuthViewModel

/**
 * LoginActivity provides the user interface for the user login process.
 * Users can enter their email and password to login.
 * It also provides an option to navigate to the registration screen.
 *
 * The activity observes the authentication result to handle login success or failure.
 */
class LoginActivity : AppCompatActivity() {

    // View binding instance for accessing views in the activity's layout.
    private lateinit var binding: ActivityLoginBinding

    // Obtain an instance of AuthViewModel to handle authentication logic.
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using view binding, making views accessible via binding.
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listener for the login button.
        binding.buttonLogin.setOnClickListener {
            // Retrieve the email and password inputs from the user.
            // Email is trimmed to remove any leading or trailing whitespace.
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString()
            // Trigger the login process via the AuthViewModel.
            authViewModel.login(email, password)
        }

        // Set click listener for the "Go To Register" button to navigate to the registration screen.
        binding.buttonGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Observe the authentication result from the AuthViewModel.
        // The result is a Pair where the first element indicates success and the second contains an error message, if any.
        authViewModel.authResult.observe(this) { (success, errorMsg) ->
            if (success) {
                // If login is successful, navigate to MainActivity.
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // If login fails, display the error message and make the error text visible.
                binding.errorText.text = errorMsg
                binding.errorText.visibility = View.VISIBLE
            }
        }
    }
}
