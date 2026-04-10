package com.aditya1875.payu.ui.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aditya1875.payu.ui.components.BottomNavbar
import com.aditya1875.payu.ui.navigation.Route
import com.aditya1875.payu.ui.presentation.balances.screens.BalancesScreen
import com.aditya1875.payu.ui.presentation.home.screens.HomeScreen
import com.aditya1875.payu.ui.presentation.profile.screens.ProfileScreen
import com.aditya1875.payu.ui.presentation.insights.screens.InsightsScreen

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

            composable(Route.Balances.route) {
                BalancesScreen()
            }

            composable(Route.Insights.route) {
                InsightsScreen()
            }

            composable(Route.Profile.route) {
                ProfileScreen()
            }
        }
    }
}



