package com.example.budgetbuddy

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class BudgetBuddyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BudgetBuddyApplication)
            modules(appModule)
        }
    }
}