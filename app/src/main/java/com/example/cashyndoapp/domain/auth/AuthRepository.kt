package com.example.cashyndoapp.domain.auth

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

interface AuthRepository {
    suspend fun signInWithGoogle(idToken: String): FirebaseUser?
    fun signOut()
    fun getCurrentUser(): FirebaseUser?
}
