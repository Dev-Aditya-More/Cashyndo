package com.aditya1875.payu.ui.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aditya1875.payu.domain.repository.auth.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
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

    private val _user = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val user: StateFlow<FirebaseUser?> = _user

    private val _state = MutableStateFlow(
        AuthState(isAuthenticated = auth.currentUser != null)
    )
    val state: StateFlow<AuthState> = _state.asStateFlow()

    // ── Sign In ───────────────────────────────────────────────────────────────

    fun signIn(email: String, password: String) {
        val emailTrimmed = email.trim()

        when {
            emailTrimmed.isBlank() && password.isBlank() ->
                return _state.update { it.copy(errorMessage = "Please enter your email and password.") }

            emailTrimmed.isBlank() ->
                return _state.update { it.copy(errorMessage = "Please enter your email address.") }

            !emailTrimmed.contains("@") ->
                return _state.update { it.copy(errorMessage = "That doesn't look like a valid email address.") }

            password.isBlank() ->
                return _state.update { it.copy(errorMessage = "Please enter your password.") }
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                auth.signInWithEmailAndPassword(emailTrimmed, password).await()
                _user.value = auth.currentUser
                _state.update { it.copy(isLoading = false, isAuthenticated = true) }
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Incorrect email or password. Please try again."
                    )
                }
            } catch (e: FirebaseAuthInvalidUserException) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No account found with this email. Try signing up instead."
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = friendlyError(e)
                    )
                }
            }
        }
    }

    // ── Sign Up ───────────────────────────────────────────────────────────────

    fun signUp(fullName: String, email: String, password: String, confirmPassword: String) {
        val nameTrimmed = fullName.trim()
        val emailTrimmed = email.trim()

        when {
            nameTrimmed.isBlank() ->
                return _state.update { it.copy(errorMessage = "Please enter your full name.") }

            emailTrimmed.isBlank() ->
                return _state.update { it.copy(errorMessage = "Please enter your email address.") }

            !emailTrimmed.contains("@") ->
                return _state.update { it.copy(errorMessage = "That doesn't look like a valid email address.") }

            password.isBlank() ->
                return _state.update { it.copy(errorMessage = "Please create a password.") }

            password.length < 8 ->
                return _state.update { it.copy(errorMessage = "Password must be at least 8 characters long.") }

            !password.any { it.isDigit() } ->
                return _state.update { it.copy(errorMessage = "Password must include at least one number.") }

            confirmPassword.isBlank() ->
                return _state.update { it.copy(errorMessage = "Please confirm your password.") }

            password != confirmPassword ->
                return _state.update { it.copy(errorMessage = "Passwords don't match. Please re-enter them.") }
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val result = auth.createUserWithEmailAndPassword(emailTrimmed, password).await()

                // Set display name
                result.user?.updateProfile(
                    UserProfileChangeRequest.Builder().setDisplayName(nameTrimmed).build()
                )?.await()

                // Send email verification
                try {
                    result.user?.sendEmailVerification()?.await()
                } catch (e: Exception) {
                    Log.w("AuthViewModel", "Email verification send failed: ${e.message}")
                }

                _user.value = auth.currentUser
                _state.update {
                    it.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        successMessage = "Account created! Check your inbox to verify your email."
                    )
                }
            } catch (e: FirebaseAuthWeakPasswordException) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Password is too weak. Use at least 8 characters with a mix of letters and numbers."
                    )
                }
            } catch (e: FirebaseAuthUserCollisionException) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "An account with this email already exists. Try signing in instead."
                    )
                }
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "That email address doesn't look valid. Please double-check it."
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = friendlyError(e)
                    )
                }
            }
        }
    }

    // ── Forgot Password ───────────────────────────────────────────────────────

    fun resetPassword(email: String) {
        val emailTrimmed = email.trim()
        when {
            emailTrimmed.isBlank() ->
                return _state.update { it.copy(errorMessage = "Please enter your email address.") }

            !emailTrimmed.contains("@") ->
                return _state.update { it.copy(errorMessage = "That doesn't look like a valid email address.") }
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            try {
                auth.sendPasswordResetEmail(emailTrimmed).await()
                _state.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Reset link sent! Check your inbox (and spam folder)."
                    )
                }
            } catch (e: FirebaseAuthInvalidUserException) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No account found with that email address."
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = friendlyError(e)
                    )
                }
            }
        }
    }

    // ── Google Sign-In ────────────────────────────────────────────────────────

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val result = repo.signInWithGoogle(idToken)
                _user.value = result
                if (result != null) {
                    Log.d("AuthViewModel", "Google sign-in success: ${result.email}")
                    _state.update { it.copy(isLoading = false, isAuthenticated = true) }
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Google sign-in failed. Please try again."
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Google sign-in error: ${e.message}")
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Google sign-in failed: ${friendlyError(e)}"
                    )
                }
            }
        }
    }

    fun signOut() {
        repo.signOut()
        _user.value = null
        _state.update { AuthState(isAuthenticated = false) }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }

    fun clearMessages() {
        _state.update { it.copy(errorMessage = null, successMessage = null) }
    }

    private fun friendlyError(e: Exception): String {
        val msg = e.message?.lowercase() ?: ""
        return when {
            msg.contains("network") -> "No internet connection. Please check your network."
            msg.contains("timeout") -> "Request timed out. Please try again."
            msg.contains("too-many-requests") -> "Too many attempts. Please wait a moment and try again."
            msg.contains("user-disabled") -> "This account has been disabled. Contact support."
            else -> "Something went wrong. Please try again."
        }
    }
}

data class AuthState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)