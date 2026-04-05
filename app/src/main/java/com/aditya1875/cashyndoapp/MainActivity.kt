package com.aditya1875.cashyndoapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aditya1875.cashyndoapp.ui.auth.AuthScreen
import com.aditya1875.cashyndoapp.ui.navigation.Route
import com.aditya1875.cashyndoapp.ui.presentation.AnimatedSplashScreen
import com.aditya1875.cashyndoapp.ui.presentation.MainNavGraph
import com.aditya1875.cashyndoapp.ui.theme.CashyndoAppTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            CashyndoAppTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Route.Splash.route
                ) {
                    composable(Route.Splash.route) {
                        AnimatedSplashScreen(navController)
                    }

                    composable(Route.Auth.route) {
                        AuthScreen(koinViewModel(), koinInject(), navController)
                    }

                    composable(Route.Main.route) {
                        MainNavGraph()
                    }
                }
            }
        }
    }
}