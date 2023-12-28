package com.example.composeapp.di

import com.example.composeapp.TestsViewModel
import com.example.composeapp.test_screens.battery.domain.BatteryRepository
import com.example.composeapp.test_screens.battery.BatteryScreenViewModel
import com.example.composeapp.test_screens.camera.CameraScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { BatteryRepository() }
    viewModel { BatteryScreenViewModel(get()) }
    viewModel { CameraScreenViewModel() }
    viewModel { TestsViewModel() }

}