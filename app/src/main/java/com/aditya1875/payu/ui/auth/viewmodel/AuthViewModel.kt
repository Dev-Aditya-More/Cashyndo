package com.aditya1875.payu.ui.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aditya1875.payu.domain.repository.auth.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(
    private val repo: AuthRepository
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user: StateFlow<FirebaseUser?> = _user

    private val _state = MutableStateFlow(
        AuthState(isAuthenticated = auth.currentUser != null)
    )

    val state: StateFlow<AuthState> = _state.asStateFlow()

    init {
        _user.value = repo.getCurrentUser()
    }

    fun signIn(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.update { it.copy(errorMessage = "Please fill in all fields") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                _state.update { it.copy(isLoading = false, isAuthenticated = true) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Sign in failed"
                    )
                }
            }
        }
    }

    fun resetPassword(email: String) {
        if (email.isBlank()) {
            _state.update { it.copy(errorMessage = "Email required") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                auth.sendPasswordResetEmail(email).await()

                _state.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Reset link sent to your email"
                    )
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Invalid email or user not found"
                    )
                }
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            val result = repo.signInWithGoogle(idToken)
            _user.value = result
            Log.d("AuthViewModel", "Signed in user: ${result?.email}")

        }
    }

    fun signUp(fullName: String, email: String, password: String, confirmPassword: String) {
        if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
            _state.update { it.copy(errorMessage = "Please fill in all fields") }
            return
        }
        if (password != confirmPassword) {
            _state.update { it.copy(errorMessage = "Passwords do not match") }
            return
        }
        if (password.length < 6) {
            _state.update { it.copy(errorMessage = "Password must be at least 6 characters") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(fullName)
                    .build()
                result.user?.updateProfile(profileUpdates)?.await()
                _state.update { it.copy(isLoading = false, isAuthenticated = true) }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Sign up failed"
                    )
                }
            }
        }
    }

    fun signOut() {
        repo.signOut()
        _user.value = null
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }
}

data class AuthState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)