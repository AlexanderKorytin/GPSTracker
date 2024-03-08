package com.example.korytingpstracker.settings.ui.domain.api

interface SettingsRepository {
    fun saveIsNeedDialogShow(value: Boolean)

    fun getColorLocationTrackLine(): Int

    fun getLocationUpdateTime(): Long
}