package com.example.composeapp.base.ui

import com.example.test_core.model.BaseTest

fun Int.toMmSs(): String {
    val minutes = this / 60
    val seconds = this % 60
    val stringMinutes = if (minutes < 10) "0$minutes" else minutes.toString()
    val stringSeconds = if (seconds < 10) "0$seconds" else seconds.toString()
    return "$stringMinutes:$stringSeconds"
}

fun List<BaseTest>.hasNext(index: Int): Boolean = index <= this.lastIndex