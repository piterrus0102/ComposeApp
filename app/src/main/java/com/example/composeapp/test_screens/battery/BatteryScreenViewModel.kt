package com.example.composeapp.test_screens.battery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature_test_battery.IBatteryTest
import com.example.feature_test_battery.TimerTicker
import com.example.test_core.data.TestResultValue
import com.example.test_core.data.TestState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BatteryScreenViewModel(private val test: IBatteryTest) : ViewModel() {

    // состояние экрана, сопровождающего тест
    private val batteryMutableState = MutableStateFlow(BatteryState())
    val batteryState = batteryMutableState.asStateFlow()

    private var batteryLevel: Int = 0

    init {
        batteryMutableState.value = batteryState.value.copy(
            options = test.options.toImmutableList(),
        )

        viewModelScope.launch {
            test.testState.collect {
                applyTestAction()
            }
        }
    }

    // получение данных от батареи (уровень заряда и факт подключенного зарядного устройства)
    fun updateBatteryIndicators(batteryLevel: Int, isCharging: Boolean) {
        this.batteryLevel = batteryLevel
        test.setEndBatteryLevel(batteryLevel)
        val newOptions = batteryMutableState.value.options.map {
            when (it.name) {
                "minChargeLevel" -> {
                    it.copy(available = batteryLevel >= it.value!!)
                }

                "currentChargeLevel" -> {
                    it.copy(value = batteryLevel)
                }

                else -> {
                    it
                }
            }
        }.filter { it.showedInList }.toImmutableList()
        batteryMutableState.value = batteryState.value.copy(
            batteryLevel = batteryLevel,
            chargeState = if (isCharging) ChargeState.Charging else ChargeState.NotCharging,
            options = newOptions,
        )
        applyTestAction()
    }

    var testsResults = mutableMapOf<IBatteryTest, TestResultValue>()
        private set

    fun intentToTestAction(isSkipped: Boolean) {
        if (isSkipped) {
            test.testMutableState.value = TestState.HardStopped
            return
        }
        when (batteryState.value.screenState) {
            is BatteryScreenState.NotExecuting -> {
                test.testMutableState.value = TestState.Execute
            }

            else -> return
        }
    }

    private fun applyTestAction() {
        val chargeState = batteryState.value.chargeState
        when (test.testState.value) {
            is TestState.Execute -> {
                if (chargeState == ChargeState.NotCharging) {
                    test.setStartBatteryLevel(batteryLevel)
                    test.timerTicker = object : TimerTicker {
                        override fun onUpdateTimerInSeconds(seconds: Int) {
                            batteryMutableState.value = batteryState.value.copy(
                                screenState = BatteryScreenState.Executing(
                                    timeToCompletionInSeconds = seconds,
                                    enabledVibro = test.testOptions.enableVibro,
                                    enabled3D = test.testOptions.enable3d
                                )
                            )
                        }

                    }
                    test.execute()
                } else {
                    test.hardStop()
                }
            }

            is TestState.HardStopped -> {
                test.hardStop()
            }

            is TestState.Completed -> {
                testActionToBatteryStateMutation(test.getTestResultValue())
            }

            else -> return
        }
    }

    private fun testActionToBatteryStateMutation(result: TestResultValue) {
        testsResults[test] = result
        batteryMutableState.value = batteryState.value.copy(
            screenState = BatteryScreenState.NotExecuting,
            testResultValue = result
        )
    }

}

data class BatteryState(
    val chargeState: ChargeState = ChargeState.Unknown,
    val screenState: BatteryScreenState = BatteryScreenState.NotExecuting,
    val testResultValue: TestResultValue = TestResultValue.UNKNOWN,
    val batteryLevel: Int = 0,
    val dischargeThreshold: Int = 0,
    val testTime: Int = 0,
    val options: ImmutableList<com.example.test_core.data.BaseTestOption> = persistentListOf()
) {
    val readyForTest =
        chargeState == ChargeState.NotCharging && options.all { it.available != false }
}

sealed class BatteryScreenState {
    data object NotExecuting : BatteryScreenState()
    data class Executing(
        val timeToCompletionInSeconds: Int,
        val enabledVibro: Boolean,
        val enabled3D: Boolean
    ) : BatteryScreenState()
}