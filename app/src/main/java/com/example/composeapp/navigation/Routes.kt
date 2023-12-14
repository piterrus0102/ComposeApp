package com.example.composeapp.navigation

sealed class Routes(val route: String) {
    object BatteryTestScreen : Routes("BatteryScreen")
    object FinalPriceScreen : Routes("FinalPriceScreen")
}
