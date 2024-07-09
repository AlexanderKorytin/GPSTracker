package com.example.korytingpstracker.main_menu.data

import com.example.korytingpstracker.main_menu.data.api.MapClient
import com.example.korytingpstracker.main_menu.data.service.LocationService
import com.example.korytingpstracker.main_menu.domain.api.MainRepository
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider

class MainRepositoryImpl(
    private val mapClient: MapClient
) : MainRepository {
    override fun configureMap() {
        mapClient.configureMap()
    }

    override fun getLocationProvider(): GpsMyLocationProvider = mapClient.getGPSProvider()
    override fun getLocationServiceState(): Boolean {
        return LocationService.getStateService()
    }

    override fun setStartTime(time: Long) {
        LocationService.setStartTime(time)
    }

    override fun getStartTime(): Long = LocationService.getStartTime()
}