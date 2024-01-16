package com.example.korytingpstracker.main_menu.data.api

import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider

interface MapClient {
    fun configureMap()

    fun getGPSProvider(): GpsMyLocationProvider
}