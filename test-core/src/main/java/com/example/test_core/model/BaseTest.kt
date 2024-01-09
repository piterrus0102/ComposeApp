package com.example.test_core.model

import com.example.test_core.data.BaseTestOption
import com.example.test_core.data.TestResultValue

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

}