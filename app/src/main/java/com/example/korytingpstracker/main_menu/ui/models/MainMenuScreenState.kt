package com.example.korytingpstracker.main_menu.ui.models

import org.osmdroid.util.GeoPoint

sealed interface MainMenuScreenState {
    data class Content(
        val speed: String = SPEED_START,
        val distance: String = DISTANCE_START,
        val averageSpeed: String = SPEED_START,
        val geoPointList: List<GeoPoint> = emptyList()
    ): MainMenuScreenState
}

private const val SPEED_START = "0.0"
private const val DISTANCE_START = "0"