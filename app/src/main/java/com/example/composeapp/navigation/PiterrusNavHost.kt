package com.example.composeapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.composeapp.final_price.FinalPriceScreen
import com.example.composeapp.test_screens.battery.BatteryScreen
import com.example.composeapp.test_screens.camera.CameraScreen

@Composable
fun PiterrusNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Routes.CameraTestScreen.route
    ) {
        composable(Routes.CameraTestScreen.route) {
            CameraScreen(navController = navController)
        }
        composable(Routes.BatteryTestScreen.route) {
            BatteryScreen(navController = navController)
        }
        composable(Routes.FinalPriceScreen.route) {
            val result: com.example.test_core.data.TestResultValue = it.arguments?.getSerializable("result") as com.example.test_core.data.TestResultValue
            FinalPriceScreen(
                testResultValue = result,
                navController = navController
            )
        }
    }
}