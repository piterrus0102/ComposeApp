package com.example.feature_test_camera

import com.example.test_core.data.BaseTestOption
import com.example.test_core.data.OptionMeasurementType
import com.example.test_core.data.TestOptionType

class BackCameraTest : ICameraTest() {

    override var options: List<BaseTestOption> = listOf(
        BaseTestOption(
            name = "flash",
            value = null,
            isInvolved = true,
            optionMeasurement = OptionMeasurementType.UNDEFINED,
            testOptionType = TestOptionType.CAMERA_FLASH
        )
    )

    override fun execute() {}

    override fun stop() {}

    override fun hardStop() {}

    override var isRunning = true
}
