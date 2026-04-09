package com.aditya1875.payu.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.aditya1875.payu.ui.navigation.Route

@Composable
fun BottomNavbar(navController: NavController) {

    val items = listOf(
        BottomNavItem(Route.Home, Icons.Default.Home, "Home"),
        BottomNavItem(Route.Transactions, Icons.Default.AccountBalanceWallet, "Balances"),
        BottomNavItem(Route.Profile, Icons.Default.Person, "Profile")
    )

    val currentRoute = navController
        .currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route.route,
                onClick = {
                    navController.navigate(item.route.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

data class BottomNavItem(
    val route: Route,
    val icon: ImageVector,
    val label: String
)


