package com.aditya1875.cashyndoapp.ui.navigation

sealed class Route(val route: String) {

    // Root Graph
    object Splash : Route("splash")
    object Auth : Route("auth")
    object Main : Route("main")

    // Bottom Navigation
    object Home : Route("home")
    object Transactions : Route("transactions")
    object Analytics : Route("analytics")
    object Profile : Route("profile")

    // Example with argument (future use)
    object TransactionDetail : Route("transaction_detail/{id}") {
        fun createRoute(id: String) = "transaction_detail/$id"
    }
}