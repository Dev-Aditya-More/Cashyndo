package com.example.cashyndoapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cashyndoapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import androidx.compose.ui.text.googlefonts.Font as GoogleFontInstance


@Composable
fun HomeScreen() {
    val user = remember { getCurrentUser() }
    val fullName = FirebaseAuth.getInstance().currentUser?.displayName
    val firstName = fullName?.split(" ")?.firstOrNull() ?: "there"

    val fontProvider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

    fun soraFont() = FontFamily(GoogleFontInstance(GoogleFont("Sora"), fontProvider))
    fun interFont() = FontFamily(GoogleFontInstance(GoogleFont("Inter"), fontProvider))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Greeting
        Text(
            text = "Hi, $firstName 👋",
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = soraFont()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Centered Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "You're signed in!",
                fontSize = 24.sp,
                fontFamily = interFont()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                // navController.navigate("explore")
            }) {
                Text("Start Exploring")
            }
        }
    }


}


fun getCurrentUser(): FirebaseUser? {
    return FirebaseAuth.getInstance().currentUser
}

