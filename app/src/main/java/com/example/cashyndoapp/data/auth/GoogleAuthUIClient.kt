package com.example.cashyndoapp.data.auth

import android.content.Context
import android.content.Intent
import com.example.cashyndoapp.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.tasks.await
import kotlin.getValue

class GoogleAuthUIClient(private val context: Context) {
    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id)) // from google-services.json
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    fun getSignInIntent(): Intent = googleSignInClient.signInIntent

    suspend fun getSignedInAccountFromIntent(intent: Intent): GoogleSignInAccount? {
        return try {
            GoogleSignIn.getSignedInAccountFromIntent(intent).await()
        } catch (e: Exception) {
            null
        }
    }

    fun signOut() {
        googleSignInClient.signOut()
    }
}
