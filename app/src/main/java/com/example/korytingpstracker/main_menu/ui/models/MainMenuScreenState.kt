package com.example.korytingpstracker.main_menu.ui.models

import org.osmdroid.util.GeoPoint

sealed interface MainMenuScreenState {
    data class Content(
        val speed: Float = 0f,
        val distance: Float = 0f,
        val averageSpeed: Float = 0f,
        val geoPointList: List<GeoPoint> = emptyList()
    ): MainMenuScreenState
}