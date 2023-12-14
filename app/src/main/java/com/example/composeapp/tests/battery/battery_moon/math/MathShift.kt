package com.example.composeapp.tests.battery.battery_moon.math

fun Double.calculateMemory(): String {
    var bit = this.toLong()
    --bit
    bit = bit or 1 //in java it's operator >>
    bit = bit or 2
    bit = bit or 4
    bit = bit or 8
    bit = bit or 16
    ++bit
    return bit.toInt().toString()
}