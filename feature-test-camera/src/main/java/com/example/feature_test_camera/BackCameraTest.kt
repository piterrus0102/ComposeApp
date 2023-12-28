package com.example.feature_test_camera

import com.example.test_core.data.BaseTestOption

class BackCameraTest : ICameraTest {

    override var options: List<BaseTestOption> = listOf(
        BaseTestOption(
            name = "flash",
            value = null,
            isInvolved = true
        )
    )

    override fun execute() {}

    override fun stop() {}

    override fun hardStop() {}

    override var isRunning = true
}
