package com.example.composeapp

import androidx.lifecycle.ViewModel
import com.example.test_core.data.TestResultValue
import com.example.test_core.model.BaseTest

class TestsViewModel(
    testsProvider: TestsProvider
) : ViewModel() {

    val tests = testsProvider.getTests()
    val distinctTests = testsProvider.getTests().groupBy { it::class.nestedClasses }.map { it.value.first() }
    var currentIndex = 0

    fun saveResult(map: Map<out BaseTest, TestResultValue>) {
        results.putAll(map)
        currentIndex++
    }

    fun clearResult(test: BaseTest) {
        results.remove(test)
        currentIndex--
    }

    fun clearResults() {
        results.clear()
        currentIndex = 0
    }

    val results = mutableMapOf<BaseTest, TestResultValue>()

}