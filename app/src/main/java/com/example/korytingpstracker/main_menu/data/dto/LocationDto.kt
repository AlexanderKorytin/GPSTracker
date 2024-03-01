package com.example.korytingpstracker.main_menu.data.dto

import org.osmdroid.util.GeoPoint
import java.io.Serializable

data class LocationDto(
    val speed: Float = 0f,
    val distance: Float = 0f,
    val geoPointList: ArrayList<GeoPoint>,
): Serializable
