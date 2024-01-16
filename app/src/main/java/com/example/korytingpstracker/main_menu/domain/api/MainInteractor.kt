package com.example.korytingpstracker.main_menu.domain.api

import com.example.korytingpstracker.main_menu.domain.models.LocationProvider

interface MainInteractor {
    fun configureMap()

    fun getLocationProvider(): LocationProvider
}