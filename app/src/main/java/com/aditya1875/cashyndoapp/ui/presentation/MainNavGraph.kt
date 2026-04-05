package com.aditya1875.cashyndoapp.ui.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aditya1875.cashyndoapp.ui.components.BottomNavbar
import com.aditya1875.cashyndoapp.ui.navigation.Route
import com.aditya1875.cashyndoapp.ui.presentation.analytics.screens.AnalyticsScreen
import com.aditya1875.cashyndoapp.ui.presentation.home.screens.HomeScreen
import com.aditya1875.cashyndoapp.ui.presentation.profile.screens.ProfileScreen
import com.aditya1875.cashyndoapp.ui.presentation.transaction.screens.TransactionsScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavGraph() {

    val bottomNavController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavbar(navController = bottomNavController)
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = Route.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Route.Home.route) {
                HomeScreen()
            }

            composable(Route.Transactions.route) {
                TransactionsScreen(navController = bottomNavController)
            }

            composable(Route.Analytics.route) {
                AnalyticsScreen(bottomNavController)
            }

            composable(Route.Profile.route) {
                ProfileScreen(bottomNavController)
            }
        }
    }
}



