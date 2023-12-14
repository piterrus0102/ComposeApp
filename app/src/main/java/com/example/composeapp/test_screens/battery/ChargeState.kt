package com.example.composeapp.test_screens.battery

sealed class ChargeState {
    object Unknown : ChargeState()
    object Charging : ChargeState()
    object NotCharging : ChargeState()
}
