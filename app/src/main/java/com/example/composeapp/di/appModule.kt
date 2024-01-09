package com.example.composeapp.di

import com.example.composeapp.TestsProvider
import com.example.composeapp.TestsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { TestsViewModel(get()) }
    single { TestsProvider() }
}