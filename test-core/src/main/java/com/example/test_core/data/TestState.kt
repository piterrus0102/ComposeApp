package com.example.test_core.data

sealed class TestState {
    object Initial : TestState()
    object Execute : TestState()
    object HardStopped : TestState()
    object Completed : TestState()
}

