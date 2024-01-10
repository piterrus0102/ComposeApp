package com.example.test_core.data

data class BaseTestOption(
    val name: String,
    val value: Int?,
    val isInvolved: Boolean = false,
    val optionMeasurement: OptionMeasurementType,
    val testOptionType: TestOptionType
)

enum class OptionMeasurementType {
    UNDEFINED,
    TIME,
    PERCENT
}