package com.example.korytingpstracker.settings.ui.data.impl

import com.example.korytingpstracker.settings.ui.data.storage.SettingsStorage
import com.example.korytingpstracker.settings.ui.domain.api.SettingsRepository

class SettingsRepositoryImpl(private val settingsStorage: SettingsStorage) : SettingsRepository {
    override fun saveIsNeedDialogShow(value: Boolean) {
        settingsStorage.saveIsNeedDialogShow(value)
    }

    override fun getColorLocationTrackLine(): Int {
        return settingsStorage.getColorLocationTrackLine()
    }

    override fun getLocationUpdateTime(): Long {
        return settingsStorage.getLocationUpdateTime()
    }


}