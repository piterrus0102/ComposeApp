package com.example.composeapp.di

import com.example.composeapp.test_screens.battery.BatteryRepository
import com.example.composeapp.test_screens.battery.BatteryScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { BatteryRepository() }
    viewModel { BatteryScreenViewModel(get()) }
}