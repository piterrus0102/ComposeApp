package com.example.composeapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.composeapp.TestsViewModel
import com.example.composeapp.base.hasNext
import com.example.composeapp.final_price.FinalPriceScreen
import com.example.composeapp.test_screens.battery.BatteryScreen
import com.example.composeapp.test_screens.camera.CameraScreen
import com.example.feature_test_battery.BatteryLoad
import com.example.feature_test_battery.IBatteryTest
import com.example.feature_test_camera.ICameraTest
import com.example.test_core.data.TestResultValue
import com.example.test_core.model.BaseTest

@Composable
fun PiterrusNavHost(
    navController: NavHostController,
    viewModel: TestsViewModel
) {
    NavHost(
        navController = navController,
        startDestination = getNextDestination(viewModel)
    ) {
        composable(Routes.CameraTestScreen.route) {
            CameraScreen(
                tests = viewModel.tests.filterIsInstance<ICameraTest>(),
                onFinishTests = {
                    viewModel.saveResult(it)
                    navController.navigate(getNextDestination(viewModel))
                }
            )
        }
        composable(Routes.BatteryTestScreen.route) {
            BatteryScreen(
                tests = viewModel.tests.filterIsInstance<IBatteryTest>(),
                onFinishTests = {
                    viewModel.saveResult(it)
                    navController.navigate(getNextDestination(viewModel))
                }
            )
        }
        composable(Routes.FinalPriceScreen.route) {
            FinalPriceScreen(
                testResultValue = viewModel.results.entries.firstOrNull { it.key is BatteryLoad }?.value ?: TestResultValue.UNKNOWN,
                onButtonClicked = {
                    viewModel.clearResults()
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(route = navController.graph.startDestinationRoute, inclusive = true)
                        .build()
                    navController.navigate(navController.graph.startDestinationRoute!!, navOptions)
                }
            )
        }
    }
}

private fun getNextDestination(viewModel: TestsViewModel): String {
    return if (viewModel.distinctTests.hasNext(viewModel.currentIndex)) {
        computeScreen(viewModel.distinctTests[viewModel.currentIndex])
    } else {
        Routes.FinalPriceScreen.route
    }
}

private fun computeScreen(test: BaseTest): String = when(test) {
        is ICameraTest -> Routes.CameraTestScreen.route
        is BatteryLoad -> Routes.BatteryTestScreen.route
        else -> throw IllegalStateException("No screens for that test")
    }
