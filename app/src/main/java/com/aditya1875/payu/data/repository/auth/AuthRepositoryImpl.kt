package com.aditya1875.payu.data.repository.auth

import android.util.Log
import com.aditya1875.payu.domain.repository.auth.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): FirebaseUser? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user
        } catch (e: Exception) {
            Log.e("AuthRepo", "Email Sign-In failed", e)
            throw e   // IMPORTANT → let ViewModel handle error
        }
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String
    ): FirebaseUser? {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user
        } catch (e: Exception) {
            Log.e("AuthRepo", "Sign-Up failed", e)
            throw e
        }
    }

    override suspend fun signInWithGoogle(idToken: String): FirebaseUser? {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        return try {
            val result = auth.signInWithCredential(credential).await()
            result.user
        } catch (e: Exception) {
            Log.e("AuthRepo", "Google Sign-In failed", e)
            throw e
        }
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun getCurrentUser(): FirebaseUser? = auth.currentUser
}