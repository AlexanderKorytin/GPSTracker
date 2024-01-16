package com.example.korytingpstracker.main_menu.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.korytingpstracker.main_menu.domain.api.MainInteractor

class MainViewModel(private val interactor: MainInteractor) : ViewModel() {
    fun configureMap() {
        interactor.configureMap()
    }
}