package com.example.composeapp

import com.example.feature_test_battery.BatteryLoad
import com.example.test_core.data.BaseTestOption
import com.example.test_core.data.OptionMeasurementType
import com.example.test_core.model.BaseTest

class TestsProvider {

    fun getTests(): List<BaseTest> {
        return listOf(
//            BackCameraTest(),
//            FrontCameraTest(),
            BatteryLoad(
                options = listOf(
                    BaseTestOption(
                        name = "currentChargeLevel",
                        optionDisplayName = "Current Charge Level",
                        value = 0,
                        optionMeasurement = OptionMeasurementType.PERCENT,
                        showedInList = true,
                        isInvolved = true

                    ),
                    BaseTestOption(
                        name = "minChargeLevel",
                        optionDisplayName = "Min Charge Level",
                        value = 1,
                        optionMeasurement = OptionMeasurementType.PERCENT,
                        showedInList = true,
                        isInvolved = true

                    ),
                    BaseTestOption(
                        name = "testTime",
                        optionDisplayName = "Test time",
                        value = 5,
                        optionMeasurement = OptionMeasurementType.TIME,
                        showedInList = true,
                        isInvolved = true
                    ),
                    BaseTestOption(
                        name = "dischargeThreshold",
                        optionDisplayName = "Discharge threshold",
                        value = 1,
                        optionMeasurement = OptionMeasurementType.PERCENT,
                        showedInList = true,
                        isInvolved = true
                    )
                )
            )
        )
    }

}