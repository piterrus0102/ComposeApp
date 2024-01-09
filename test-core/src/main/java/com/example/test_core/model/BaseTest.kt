package com.example.test_core.model

import com.example.test_core.data.BaseTestOption
import com.example.test_core.data.TestResultValue
import com.example.test_core.data.TestState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


abstract class BaseTest {
    abstract fun execute()
    abstract fun stop()
    abstract fun hardStop()
    abstract var isRunning: Boolean
    abstract var options: List<BaseTestOption>

    private var testResultValue = TestResultValue.UNKNOWN

    fun setTestResultValue(testResultValue: TestResultValue) {
        this.testResultValue = testResultValue
    }

    fun getTestResultValue(): TestResultValue = testResultValue

    val testMutableState = MutableStateFlow<TestState>(TestState.Initial)
    val testState = testMutableState.asStateFlow()

}