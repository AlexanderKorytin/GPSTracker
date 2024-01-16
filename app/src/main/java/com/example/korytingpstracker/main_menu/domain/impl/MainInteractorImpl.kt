package com.example.korytingpstracker.main_menu.domain.impl

import com.example.korytingpstracker.main_menu.domain.api.MainInteractor
import com.example.korytingpstracker.main_menu.domain.api.MainRepository

class MainInteractorImpl(private val repository: MainRepository) : MainInteractor {
    override fun configureMap() {
        repository.configureMap()
    }

}