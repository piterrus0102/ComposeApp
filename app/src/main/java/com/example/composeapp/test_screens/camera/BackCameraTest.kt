package com.example.composeapp.test_screens.camera

import androidx.compose.runtime.Stable
import com.example.test_core.data.BaseTestOption
import okhttp3.internal.immutableListOf

class BackCameraTest : ICameraTest {

    @Stable
    override var options: List<BaseTestOption> = immutableListOf(
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
