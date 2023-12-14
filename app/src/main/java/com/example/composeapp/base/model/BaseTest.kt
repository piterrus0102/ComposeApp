package com.example.composeapp.base.model

interface BaseTest {
    fun execute()
    fun stop()
    fun hardStop()
    var isRunning: Boolean
}