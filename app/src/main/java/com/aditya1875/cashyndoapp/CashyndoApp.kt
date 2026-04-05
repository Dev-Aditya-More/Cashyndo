package com.aditya1875.cashyndoapp

import android.app.Application
import com.aditya1875.cashyndoapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class CashyndoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CashyndoApp)
            modules(appModule)
        }
    }
}
