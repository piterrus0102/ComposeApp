package com.example.composeapp

import androidx.lifecycle.ViewModel
import com.example.test_core.data.TestResultValue
import com.example.test_core.model.BaseTest

class TestsViewModel : ViewModel() {

    val results = mutableMapOf<BaseTest, TestResultValue>()

}