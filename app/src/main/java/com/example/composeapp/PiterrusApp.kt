package com.example.composeapp

import android.app.Application
import com.example.composeapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PiterrusApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PiterrusApp)
            modules(appModule)
        }
    }

}