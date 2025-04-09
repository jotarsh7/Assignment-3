package com.example.favoritemovieapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * AuthViewModel handles user authentication tasks such as registration and login
 * using Firebase Authentication. It communicates authentication results via LiveData.
 */
class AuthViewModel : ViewModel() {

    // Firebase Authentication instance for performing auth operations.
    private val auth = FirebaseAuth.getInstance()

    // LiveData to expose the result of authentication operations.
    // The Pair contains a Boolean indicating success and an optional error message.
    val authResult = MutableLiveData<Pair<Boolean, String?>>()

    // Initialize the FirebaseAuth instance with preferred settings.
    init {
        // Set the language code to English for authentication emails or UI messages.
        auth.setLanguageCode("en")
    }

    /**
     * Registers a new user with the provided email and password.
     *
     * @param email The email address of the new user.
     * @param password The password for the new user.
     */
    fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registration successful; update LiveData with a success result.
                    authResult.value = Pair(true, null)
                } else {
                    // Extract error message or provide a default error message.
                    val err = task.exception?.message ?: "Registration failed."
                    // Log the error details for debugging purposes.
                    Log.e("AuthViewModel", "Registration error: $err", task.exception)
                    // Update LiveData with a failure result and the error message.
                    authResult.value = Pair(false, err)
                }
            }
    }

    /**
     * Logs in an existing user with the provided email and password.
     *
     * @param email The email address of the user.
     * @param password The password of the user.
     */
    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful; update LiveData with a success result.
                    authResult.value = Pair(true, null)
                } else {
                    // Extract error message or provide a default error message.
                    val err = task.exception?.message ?: "Login failed."
                    // Log the error details for debugging purposes.
                    Log.e("AuthViewModel", "Login error: $err", task.exception)
                    // Update LiveData with a failure result and the error message.
                    authResult.value = Pair(false, err)
                }
            }
    }
}
