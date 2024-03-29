package com.example.korytingpstracker.main_menu.domain.api

import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider

interface MainRepository {
    fun configureMap()

    fun getLocationProvider(): GpsMyLocationProvider

    fun getLocationServiceState(): Boolean

    fun setStartTime(time: Long)

    fun getStartTime(): Long
}