package com.example.test_core.data

sealed class TestState {
    object Initial : TestState()
    object Execute : TestState()
    object HardStopped : TestState()
    data class Completed(val result: TestResultValue) : TestState()
}

