package com.example.composeapp.base.data

sealed class TestState {
    object Initial : TestState()
    object Execute : TestState()
    object HardStopped : TestState()
    data class Completed(val result: TestResultValue) : TestState()
}

enum class TestResultValue {
    UNKNOWN,
    PASSED,
    FAILED,
    SKIPPED
}