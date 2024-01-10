package com.example.composeapp

import com.example.feature_test_battery.BatteryLoad
import com.example.test_core.data.BaseTestOption
import com.example.test_core.data.OptionMeasurementType
import com.example.test_core.data.TestOptionType
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
                        value = 0,
                        optionMeasurement = OptionMeasurementType.PERCENT,
                        isInvolved = true,
                        testOptionType = TestOptionType.CURRENT_CHARGE_LEVEL
                    ),
                    BaseTestOption(
                        name = "minChargeLevel",
                        value = 20,
                        optionMeasurement = OptionMeasurementType.PERCENT,
                        isInvolved = true,
                        testOptionType = TestOptionType.MIN_CHARGE_LEVEL
                    ),
                    BaseTestOption(
                        name = "testTime",
                        value = 5,
                        optionMeasurement = OptionMeasurementType.TIME,
                        isInvolved = true,
                        testOptionType = TestOptionType.TEST_TIME
                    ),
                    BaseTestOption(
                        name = "dischargeThreshold",
                        value = 1,
                        optionMeasurement = OptionMeasurementType.PERCENT,
                        isInvolved = true,
                        testOptionType = TestOptionType.DISCHARGE_THRESHOLD
                    ),
                    BaseTestOption(
                        name = "enableVibro",
                        isInvolved = true,
                        testOptionType = TestOptionType.ENABLE_VIBRO,
                        value = null,
                        optionMeasurement = OptionMeasurementType.UNDEFINED
                    ),
                    BaseTestOption(
                        name = "enable3D",
                        isInvolved = true,
                        testOptionType = TestOptionType.ENABLE_3D,
                        value = null,
                        optionMeasurement = OptionMeasurementType.UNDEFINED
                    )
                )
            )
        )
    }

}