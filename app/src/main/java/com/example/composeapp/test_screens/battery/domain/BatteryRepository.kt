package com.example.composeapp.test_screens.battery.domain

import com.example.test_core.data.TestOption

// domain layer imitation
class BatteryRepository {

    fun getOptions(): List<TestOption> = listOf(
        TestOption(
            optionName = "currentChargeLevel",
            optionDisplayName = "Current Charge Level",
            optionValue = 0,
            optionMeasurement = com.example.test_core.data.OptionMeasurementType.PERCENT,
            showedInList = true

        ),
        TestOption(
            optionName = "minChargeLevel",
            optionDisplayName = "Min Charge Level",
            optionValue = 1,
            optionMeasurement = com.example.test_core.data.OptionMeasurementType.PERCENT,
            showedInList = true

        ),
        TestOption(
            optionName = "testTime",
            optionDisplayName = "Test time",
            optionValue = 5,
            optionMeasurement = com.example.test_core.data.OptionMeasurementType.TIME,
            showedInList = true
        ),
        TestOption(
            optionName = "dischargeThreshold",
            optionDisplayName = "Discharge threshold",
            optionValue = 1,
            optionMeasurement = com.example.test_core.data.OptionMeasurementType.PERCENT,
            showedInList = true
        )
    )

}