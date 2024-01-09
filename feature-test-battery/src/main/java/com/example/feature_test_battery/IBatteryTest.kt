package com.example.feature_test_battery

import com.example.test_core.model.BaseTest

abstract class IBatteryTest : BaseTest() {
    abstract fun setStartBatteryLevel(startBatteryLevel: Int)
    abstract fun setEndBatteryLevel(endBatteryLevel: Int)
    abstract fun getRealDischargeThreshold(): Int
    abstract var timerTicker: TimerTicker?
}