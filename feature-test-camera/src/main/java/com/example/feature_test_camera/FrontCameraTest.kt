package com.example.feature_test_camera

import com.example.test_core.data.BaseTestOption

class FrontCameraTest : ICameraTest {
    override fun execute() {}

    override fun stop() {}

    override fun hardStop() {}

    override var isRunning = true

    override var options: List<BaseTestOption> = listOf()
}
