package com.example.cashyndoapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cashyndoapp.ui.components.BottomNavbar

@Composable
fun MainScreen() {

    val bottomNavController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavbar(navController = bottomNavController)
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen()
            }
            composable("transactions") {
                TransactionsScreen(bottomNavController)
            }
            composable("analytics") {
                AnalyticsScreen(bottomNavController)
            }
            composable("profile") {
                ProfileScreen(bottomNavController)
            }
        }
    }
}



