package com.example.composeapp.test_screens.battery

import com.example.composeapp.base.data.OptionMeasurementType
import com.example.composeapp.base.data.TestOption

// domain layer imitation
class BatteryRepository {

    fun getOptions(): List<TestOption> = listOf(
        TestOption(
            optionName = "currentChargeLevel",
            optionDisplayName = "Current Charge Level",
            optionValue = 0,
            optionMeasurement = OptionMeasurementType.PERCENT,
            showedInList = true

        ),
        TestOption(
            optionName = "minChargeLevel",
            optionDisplayName = "Min Charge Level",
            optionValue = 1,
            optionMeasurement = OptionMeasurementType.PERCENT,
            showedInList = true

        ),
        TestOption(
            optionName = "testTime",
            optionDisplayName = "Test time",
            optionValue = 5,
            optionMeasurement = OptionMeasurementType.TIME,
            showedInList = true
        ),
        TestOption(
            optionName = "dischargeThreshold",
            optionDisplayName = "Discharge threshold",
            optionValue = 1,
            optionMeasurement = OptionMeasurementType.PERCENT,
            showedInList = true
        )
    )

}