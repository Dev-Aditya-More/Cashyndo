package com.example.cashyndoapp.ui.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cashyndoapp.domain.auth.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repo: AuthRepository
) : ViewModel() {

    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user: StateFlow<FirebaseUser?> = _user

    init {
        _user.value = repo.getCurrentUser()
    }

    fun signIn(idToken: String) {
        viewModelScope.launch {
            val result = repo.signInWithGoogle(idToken)
            _user.value = result
            Log.d("AuthViewModel", "Signed in user: ${result?.email}")

        }
    }

    fun signOut() {
        repo.signOut()
        _user.value = null
    }
}




