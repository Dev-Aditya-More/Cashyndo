package com.aditya1875.payu.ui.navigation

sealed class Route(val route: String) {

    // Root Graph
    object Splash : Route("splash")

    object Auth : Route("auth")

    object ForgotPassword : Route("forgot_pass")

    object Main : Route("main")

    // Bottom Navigation
    object Home : Route("home")
    object Transactions : Route("transactions")
    object Profile : Route("profile")

    object TransactionDetail : Route("transaction_detail/{id}") {
        fun createRoute(id: String) = "transaction_detail/$id"
    }
}