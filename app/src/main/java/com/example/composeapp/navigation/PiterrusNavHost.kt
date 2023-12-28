package com.example.composeapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.composeapp.TestsViewModel
import com.example.composeapp.final_price.FinalPriceScreen
import com.example.composeapp.test_screens.battery.BatteryScreen
import com.example.composeapp.test_screens.camera.CameraScreen
import com.example.feature_test_battery.BatteryLoad

@Composable
fun PiterrusNavHost(
    navController: NavHostController,
    viewModel: TestsViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.CameraTestScreen.route
    ) {
        composable(Routes.CameraTestScreen.route) {
            CameraScreen(
                onFinishTests = {
                    viewModel.results.putAll(it)
                    navController.navigate(Routes.BatteryTestScreen.route)
                }
            )
        }
        composable(Routes.BatteryTestScreen.route) {
            BatteryScreen(
                onFinishTests = {
                    viewModel.results.putAll(it)
                    navController.navigate(Routes.FinalPriceScreen.route)
                }
            )
        }
        composable(Routes.FinalPriceScreen.route) {
            FinalPriceScreen(
                testResultValue = viewModel.results.entries.first { it.key is BatteryLoad }.value,
                onButtonClicked = {
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(route = Routes.CameraTestScreen.route, inclusive = true)
                        .build()
                    navController.navigate(Routes.CameraTestScreen.route, navOptions)
                }
            )
        }
    }
}