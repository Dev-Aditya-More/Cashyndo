package com.aditya1875.payu.ui.presentation.profile.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ProfileUiState(
    val name: String = "",
    val currency: String = "INR",
    val isDarkMode: Boolean = true,
    val isError : Boolean = false
)

class ProfileViewModel(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state

    init {
        loadUser()
    }

    fun updateName(name: String) {
        val user = firebaseAuth.currentUser ?: return

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()

        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _state.value = _state.value.copy(name = name)
                } else {
                    _state.value = _state.value.copy(isError = true)
                }
            }
    }

    private fun loadUser() {
        val name = firebaseAuth.currentUser?.displayName ?: "User"

        _state.value = _state.value.copy(
            name = name
        )
    }

    fun onCurrencyChange(currency: String) {
        _state.value = _state.value.copy(
            currency = currency
        )
    }

    fun onThemeChange(isDark: Boolean) {
        _state.value = _state.value.copy(
            isDarkMode = isDark
        )
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    fun resetData() {
        // later: clear database
    }

    fun exportData() {
        // later: export logic
    }
}