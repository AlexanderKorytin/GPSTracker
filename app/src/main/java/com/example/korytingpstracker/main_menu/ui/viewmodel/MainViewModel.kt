package com.example.korytingpstracker.main_menu.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.korytingpstracker.main_menu.domain.api.MainInteractor
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider

class MainViewModel(private val interactor: MainInteractor) : ViewModel() {
    private var locationProvider: MutableLiveData<GpsMyLocationProvider> = MutableLiveData(null)

    fun getLocationProviderValue(): LiveData<GpsMyLocationProvider> = locationProvider
    fun configureMap() {
        interactor.configureMap()
    }

    fun getProvider() {
        locationProvider.postValue(interactor.getLocationProvider().provider)
    }
}