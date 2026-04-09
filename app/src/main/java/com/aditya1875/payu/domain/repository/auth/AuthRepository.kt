package com.aditya1875.payu.domain.repository.auth

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    suspend fun signInWithEmail(email: String, password: String): FirebaseUser?

    suspend fun signUpWithEmail(email: String, password: String): FirebaseUser?

    suspend fun signInWithGoogle(idToken: String): FirebaseUser?

    fun signOut()

    fun getCurrentUser(): FirebaseUser?
}