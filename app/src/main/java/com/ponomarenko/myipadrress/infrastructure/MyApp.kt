package com.ponomarenko.myipadrress.infrastructure

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            modules(MainActivityInjectionModule.module)
        }
    }
}