package com.example.composeapp.test_screens.battery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.example.composeapp.android_managers.ChargeManager
import com.example.composeapp.base.AttachLifecycleEvent
import com.example.feature_test_battery.IBatteryTest
import com.example.test_core.data.TestResultValue
import com.example.test_core.model.BaseTest


@Composable
fun BatteryScreen(
    tests: List<IBatteryTest>,
    onFinishTests: (results: Map<out BaseTest, TestResultValue>) -> Unit
) {
    val viewModel = remember {
        BatteryScreenViewModel(test = tests[0])
    }
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