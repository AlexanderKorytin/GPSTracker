package com.example.korytingpstracker.settings.ui.data.storage

interface SettingsStorage {
    fun saveIsNeedDialogShow(value: Boolean)

    fun getColorLocationTrackLine(): Int

    fun getLocationUpdateTime(): Long
}