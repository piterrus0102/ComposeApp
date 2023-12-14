package com.example.composeapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.composeapp.base.data.TestResultValue
import com.example.composeapp.final_price.FinalPriceScreen
import com.example.composeapp.test_screens.battery.BatteryScreen

@Composable
fun PiterrusNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.BatteryTestScreen.route
    ) {
        composable(Routes.BatteryTestScreen.route) {
            BatteryScreen(navController = navController)
        }
        composable(Routes.FinalPriceScreen.route) {
            val result: TestResultValue = it.arguments?.getSerializable("result") as TestResultValue
            FinalPriceScreen(
                testResultValue = result,
                navController = navController
            )
        }
    }
}