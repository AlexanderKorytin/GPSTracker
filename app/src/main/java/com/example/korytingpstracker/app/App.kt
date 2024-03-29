package com.example.korytingpstracker.app

import android.app.Application
import com.example.korytingpstracker.main_menu.di.mainDataModule
import com.example.korytingpstracker.main_menu.di.mainDomainModule
import com.example.korytingpstracker.main_menu.di.mainVMModule
import com.example.korytingpstracker.settings.ui.di.settingsDataModule
import com.example.korytingpstracker.settings.ui.di.settingsDomainModule
import com.example.korytingpstracker.tracks.di.trackViewModelModule
import com.example.korytingpstracker.tracks.di.tracksDataModule
import com.example.korytingpstracker.tracks.di.tracksDomainModule
import com.markodevcic.peko.PermissionRequester
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

const val APP_SETTINGS_PREF_KEY = "App settings"
const val IS_NEED_SHOW_DIALOG = "is need show dialog"

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        PermissionRequester.initialize(applicationContext)
        startKoin {
            androidContext(applicationContext)
            modules(
                mainDataModule,
                mainDomainModule,
                mainVMModule,

                settingsDataModule,
                settingsDomainModule,

                tracksDataModule,
                tracksDomainModule,
                trackViewModelModule
            )
        }
    }
}