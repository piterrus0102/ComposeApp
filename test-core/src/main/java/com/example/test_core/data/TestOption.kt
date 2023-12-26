package com.example.test_core.data

data class TestOption(
    val optionName: String,
    val optionDisplayName: String,
    val optionValue: Int,
    val optionMeasurement: OptionMeasurementType,
    val available: Boolean? = null,
    val showedInList: Boolean
)

enum class OptionMeasurementType {
    TIME,
    PERCENT
}
