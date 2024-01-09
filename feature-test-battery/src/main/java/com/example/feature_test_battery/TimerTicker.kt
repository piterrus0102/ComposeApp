package com.example.feature_test_battery

interface TimerTicker {
    val onUpdateTimerInSeconds: (Int) -> Unit
}