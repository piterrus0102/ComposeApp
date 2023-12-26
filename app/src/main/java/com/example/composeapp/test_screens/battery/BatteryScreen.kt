package com.example.composeapp.test_screens.battery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.bundleOf
import androidx.navigation.NavHostController
import com.example.composeapp.android_managers.ChargeManager
import com.example.composeapp.base.ui.AttachLifecycleEvent
import com.example.composeapp.navigation.Routes
import org.koin.androidx.compose.koinViewModel


@Composable
fun BatteryScreen(
    navController: NavHostController
) {
    val viewModel: BatteryScreenViewModel = koinViewModel()
    // getting battery data regarding the charge in realtime
    ChargeManager { batteryLevel, isCharging ->
        viewModel.updateBatteryIndicators(
            batteryLevel = batteryLevel,
            isCharging = isCharging
        )
    }
    AttachLifecycleEvent(
        onStopCallback = {
            viewModel.intentToTestAction(isSkipped = true)
        }
    )
    BatteryView(
        batteryState = viewModel.batteryState.collectAsState().value,
        onStartButtonClicked = {
            viewModel.intentToTestAction(isSkipped = false)
        },
        onSkipButtonClicked = {
            viewModel.intentToTestAction(isSkipped = true)
        },
        onFinishedButtonClicked = {
            navController.findDestination(Routes.FinalPriceScreen.route)?.let {
                val bundle = bundleOf("result" to viewModel.getResult())
                navController.navigate(it.id, bundle)
            }
        }
    )
}