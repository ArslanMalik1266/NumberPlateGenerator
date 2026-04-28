package com.webscare.numberplategenerator

import android.app.Application
import com.webscare.numberplategenerator.di.appModule
import com.webscare.numberplategenerator.di.dataModule
import com.webscare.numberplategenerator.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApp)
            modules(listOf(dataModule, domainModule, appModule))
        }
    }
}