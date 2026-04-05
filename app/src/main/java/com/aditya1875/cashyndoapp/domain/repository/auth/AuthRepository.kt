package com.aditya1875.cashyndoapp.domain.repository.auth

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun signInWithGoogle(idToken: String): FirebaseUser?
    fun signOut()
    fun getCurrentUser(): FirebaseUser?
}
