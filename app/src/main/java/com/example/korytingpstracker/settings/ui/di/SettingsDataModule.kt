package com.example.korytingpstracker.settings.ui.di

import com.example.korytingpstracker.settings.ui.data.storage.SettingsStorage
import com.example.korytingpstracker.settings.ui.data.storage.SharedPrefStorage
import org.koin.dsl.module

val settingsDataModule = module {
    single<SettingsStorage> { SharedPrefStorage(sharedPreferences = get()) }
}