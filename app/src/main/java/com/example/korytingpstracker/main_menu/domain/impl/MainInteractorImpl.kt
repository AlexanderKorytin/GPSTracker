package com.example.korytingpstracker.main_menu.domain.impl

import com.example.korytingpstracker.main_menu.domain.api.MainInteractor
import com.example.korytingpstracker.main_menu.domain.api.MainRepository
import com.example.korytingpstracker.main_menu.domain.models.LocationProvider

class MainInteractorImpl(private val repository: MainRepository) : MainInteractor {
    override fun configureMap() {
        repository.configureMap()
    }

    override fun getLocationProvider(): LocationProvider =
        LocationProvider(repository.getLocationProvider())

    override fun getStateSeervice(): Boolean {
        return repository.getLocationServiceState()
    }

}