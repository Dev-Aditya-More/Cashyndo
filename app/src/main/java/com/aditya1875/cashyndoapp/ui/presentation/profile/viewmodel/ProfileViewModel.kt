package com.aditya1875.cashyndoapp.ui.presentation.profile.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class ProfileUiState(
    val name: String = "",
    val currency: String = "INR",
    val isDarkMode: Boolean = true
)

class ProfileViewModel(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state

    init {
        loadUser()
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