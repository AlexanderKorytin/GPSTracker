package com.example.korytingpstracker.settings.ui.domain.impl

import com.example.korytingpstracker.settings.ui.domain.api.SettingsInteractor
import com.example.korytingpstracker.settings.ui.domain.api.SettingsRepository

class SettingInteractorImpl(private val repository: SettingsRepository): SettingsInteractor {

    override fun getColorLocationTrackLine(): Int {
        return repository.getColorLocationTrackLine()
    }

    override fun getLocationUpdateTime(): Long {
        return repository.getLocationUpdateTime()
    }

}