package com.example.composeapp.test_screens.battery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.composeapp.android_managers.ChargeManager
import com.example.composeapp.base.ui.AttachLifecycleEvent
import com.example.test_core.data.TestResultValue
import com.example.test_core.model.BaseTest
import org.koin.androidx.compose.koinViewModel


@Composable
fun BatteryScreen(
    onFinishTests: (results: Map<out BaseTest, TestResultValue>) -> Unit
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
            onFinishTests.invoke(viewModel.testsResults)
        }
    )
}