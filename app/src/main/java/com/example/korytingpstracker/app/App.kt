package com.example.korytingpstracker.app

import android.app.Application
import com.example.korytingpstracker.main_menu.di.mainDataModule
import com.example.korytingpstracker.main_menu.di.mainDomainModule
import com.example.korytingpstracker.main_menu.di.mainVMModule
import com.markodevcic.peko.PermissionRequester
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        PermissionRequester.initialize(applicationContext)

        startKoin {
            androidContext(applicationContext)
            modules(
                mainDataModule,
                mainDomainModule,
                mainVMModule
            )
        }

    }
}