package com.example.korytingpstracker.settings.ui.domain.api

interface SettingsRepository {
    fun getColorLocationTrackLine(): Int
    fun getLocationUpdateTime(): Long
}