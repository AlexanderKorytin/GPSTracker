package com.example.korytingpstracker.main_menu.ui.models

import org.osmdroid.util.GeoPoint

data class LocationTrack(
    val locName: String = EMPTY_STR,
    val speed: String = SPEED_START,
    val date: String = EMPTY_STR,
    val time: String = EMPTY_STR,
    val distance: String = DISTANCE_START,
    val averageSpeed: String = SPEED_START,
    val geoPointList: List<GeoPoint> = emptyList()
)

private const val SPEED_START = "0.0"
private const val DISTANCE_START = "0"
private const val EMPTY_STR = ""