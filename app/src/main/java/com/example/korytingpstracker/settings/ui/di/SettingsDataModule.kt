package com.example.korytingpstracker.settings.ui.di

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.korytingpstracker.settings.ui.data.storage.SettingsStorage
import com.example.korytingpstracker.settings.ui.data.storage.SharedPrefStorage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val settingsDataModule = module {
    single<SharedPreferences> {
        PreferenceManager.getDefaultSharedPreferences(androidContext())
    }
    single<SettingsStorage> {
        SharedPrefStorage(
            sharedPreferences = get(),
            settingsPref = get(),
            context = androidContext()
        )
    }
}