package com.example.composeapp.base

import com.example.test_core.data.BaseTestOption
import com.example.test_core.data.OptionMeasurementType
import com.example.test_core.data.TestOptionType

data class UITestOption(
    val optionDisplayName: String,
    val optionDisplayValue: Int,
    val optionMeasurement: OptionMeasurementType = OptionMeasurementType.UNDEFINED,
    val available: Boolean? = null,
    val showedInList: Boolean = true,
    val testOptionType: TestOptionType
)

fun BaseTestOption.toUITestOption(
    optionDisplayName: String? = null,
    available: Boolean? = null,
    showedInList: Boolean = true
): UITestOption {
    return UITestOption(
        optionDisplayName = optionDisplayName ?: this.name,
        optionDisplayValue = this.value ?: -1,
        optionMeasurement = this.optionMeasurement,
        available = available,
        showedInList = showedInList,
        testOptionType = this.testOptionType
    )
}