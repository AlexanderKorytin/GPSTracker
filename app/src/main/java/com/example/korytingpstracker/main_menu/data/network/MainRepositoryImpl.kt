package com.example.korytingpstracker.main_menu.data.network

import com.example.korytingpstracker.main_menu.data.api.MapClient
import com.example.korytingpstracker.main_menu.domain.api.MainRepository
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider

class MainRepositoryImpl(private val mapClient: MapClient) : MainRepository {
    override fun configureMap() {
        mapClient.configureMap()
    }

    override fun getLocationProvider(): GpsMyLocationProvider = mapClient.getGPSProvider()
}