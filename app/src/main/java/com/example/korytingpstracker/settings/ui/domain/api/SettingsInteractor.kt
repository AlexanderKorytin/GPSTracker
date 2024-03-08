package com.example.korytingpstracker.settings.ui.domain.api

interface SettingsInteractor {
    fun saveIsNeedDialogShow(value: Boolean)
    fun getColorLocationTrackLine(): Int
    fun getLocationUpdateTime(): Long
}