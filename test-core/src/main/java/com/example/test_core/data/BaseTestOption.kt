package com.example.test_core.data

data class BaseTestOption(
    val name: String,
    val value: Int?,
    val isInvolved: Boolean,
    val optionDisplayName: String,
    val optionMeasurement: OptionMeasurementType,
    val available: Boolean? = null,
    val showedInList: Boolean
)

enum class OptionMeasurementType {
    UNDETERMINED,
    TIME,
    PERCENT
}