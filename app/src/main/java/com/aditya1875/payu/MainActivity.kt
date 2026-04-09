package com.aditya1875.payu

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aditya1875.payu.ui.auth.screens.ForgotPasswordScreen
import com.aditya1875.payu.ui.auth.screens.LoginAuthScreen
import com.aditya1875.payu.ui.navigation.Route
import com.aditya1875.payu.ui.presentation.AnimatedSplashScreen
import com.aditya1875.payu.ui.presentation.MainNavGraph
import com.aditya1875.payu.ui.theme.CashyndoAppTheme

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
                        LoginAuthScreen(navController = navController)
                    }

                    composable(Route.ForgotPassword.route) {
                        ForgotPasswordScreen(
                            navController
                        )
                    }

                    composable(Route.Main.route) {
                        MainNavGraph()
                    }
                }
            }
        }
    }
}