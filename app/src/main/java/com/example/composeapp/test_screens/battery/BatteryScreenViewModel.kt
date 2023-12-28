package com.example.composeapp.test_screens.battery

import androidx.lifecycle.ViewModel
import com.example.composeapp.test_screens.battery.domain.BatteryRepository
import com.example.feature_test_battery.BatteryLoad
import com.example.test_core.data.TestResultValue
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BatteryScreenViewModel(
    private val batteryRepository: BatteryRepository
) : ViewModel() {

    // состояние экрана, сопровождающего тест
    private val batteryMutableState = MutableStateFlow(BatteryState())
    val batteryState = batteryMutableState.asStateFlow()

    // состояние теста
    private val testMutableState =
        MutableStateFlow<com.example.test_core.data.TestState>(com.example.test_core.data.TestState.Initial)
    private val testState = testMutableState.asStateFlow()

    private var test: BatteryLoad? = null
    private var batteryLevel: Int = 0

    init {
        test = BatteryLoad(
            options = batteryRepository.getOptions(),
            // колбэк на обновление таймера на экране выполнения теста
            onUpdateTimerInSeconds = { tick, enabledVibro, enabled3D ->
                batteryMutableState.value = batteryState.value.copy(
                    screenState = BatteryScreenState.Executing(
                        tick, enabledVibro, enabled3D
                    )
                )
            },
            //колбэк с результатом выполнения теста
            onCompleted = { result, realDischargeThreshold ->
                realDischargeThreshold?.let {
                    batteryMutableState.value = batteryState.value.copy(
                        dischargeThreshold = realDischargeThreshold,
                    )
                }
                testMutableState.value = com.example.test_core.data.TestState.Completed(result)
                applyTestAction()
            },
        )
    }

    // получение данных от батареи (уровень заряда и факт подключенного зарядного устройства)
    fun updateBatteryIndicators(batteryLevel: Int, isCharging: Boolean) {
        this.batteryLevel = batteryLevel
        test?.setEndBatteryLevel(batteryLevel)
        val newOptions = batteryRepository.getOptions().map {
                when (it.optionName) {
                    "minChargeLevel" -> {
                        it.copy(available = batteryLevel >= it.optionValue)
                    }

                    "currentChargeLevel" -> {
                        it.copy(optionValue = batteryLevel)
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

    var testsResults = mutableMapOf<BatteryLoad, TestResultValue>()
        private set

    fun intentToTestAction(isSkipped: Boolean) {
        if (isSkipped) {
            testMutableState.value = com.example.test_core.data.TestState.HardStopped
            applyTestAction()
            return
        }
        when (batteryState.value.screenState) {
            is BatteryScreenState.NotExecuting -> {
                testMutableState.value = com.example.test_core.data.TestState.Execute
                applyTestAction()
            }

            is BatteryScreenState.Executing -> {}
        }
    }

    private fun applyTestAction() {
        val chargeState = batteryState.value.chargeState
        val testState = testState.value
        test?.let { test ->
            when (testState) {

                is com.example.test_core.data.TestState.Initial -> {}

                is com.example.test_core.data.TestState.Execute -> {
                    if (chargeState == ChargeState.NotCharging) {
                        if (!test.isRunning) {
                            test.setStartBatteryLevel(batteryLevel)
                            test.execute()
                        } else return
                    } else {
                        test.hardStop()
                    }
                }

                is com.example.test_core.data.TestState.HardStopped -> {
                    test.hardStop()
                }

                is com.example.test_core.data.TestState.Completed -> {
                    testActionToBatteryStateMutation(testState.result)
                }
            }
        }
    }

    private fun testActionToBatteryStateMutation(result: TestResultValue) {
        test?.let { testsResults[it] = result }
        batteryMutableState.value = batteryState.value.copy(
            screenState = BatteryScreenState.NotExecuting, testResultValue = result
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
    val options: ImmutableList<com.example.test_core.data.TestOption> = persistentListOf()
) {
    val readyForTest =
        chargeState == ChargeState.NotCharging && options.all { it.available != false }
}

sealed class BatteryScreenState {
    object NotExecuting : BatteryScreenState()
    data class Executing(
        val timeToCompletionInSeconds: Int, val enabledVibro: Boolean, val enabled3D: Boolean
    ) : BatteryScreenState()
}