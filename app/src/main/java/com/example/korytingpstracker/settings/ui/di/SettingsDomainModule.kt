package com.example.korytingpstracker.settings.ui.di

import com.example.korytingpstracker.settings.ui.data.impl.SettingsRepositoryImpl
import com.example.korytingpstracker.settings.ui.domain.api.SettingsInteractor
import com.example.korytingpstracker.settings.ui.domain.api.SettingsRepository
import com.example.korytingpstracker.settings.ui.domain.impl.SettingInteractorImpl
import org.koin.dsl.module

val settingsDomainModule = module {
    single<SettingsRepository> { SettingsRepositoryImpl(settingsStorage = get()) }
    factory<SettingsInteractor> { SettingInteractorImpl(repository = get()) }
}