package com.example.test_core.model

import com.example.test_core.data.BaseTestOption

interface BaseTest {
    fun execute()
    fun stop()
    fun hardStop()
    var isRunning: Boolean
    var options: List<BaseTestOption>
}