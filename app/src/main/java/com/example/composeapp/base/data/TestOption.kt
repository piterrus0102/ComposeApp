package com.example.composeapp.base.data

data class TestOption(
    val optionName: String,
    val optionDisplayName: String,
    val optionValue: Int,
    val optionMeasurement: OptionMeasurementType,
    val available: Boolean? = null,
    val showedInList: Boolean
)

fun Int.toMmSs(): String {
    val minutes = this / 60
    val seconds = this % 60
    val stringMinutes = if (minutes < 10) "0$minutes" else minutes.toString()
    val stringSeconds = if (seconds < 10) "0$seconds" else seconds.toString()
    return "$stringMinutes:$stringSeconds"
}

enum class OptionMeasurementType {
    TIME,
    PERCENT
}
