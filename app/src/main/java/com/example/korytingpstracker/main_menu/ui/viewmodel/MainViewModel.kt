package com.example.korytingpstracker.main_menu.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.korytingpstracker.main_menu.domain.api.MainInteractor
import com.example.korytingpstracker.settings.ui.domain.api.SettingsInteractor
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider

class MainViewModel(
    private val mainInteractor: MainInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {
    private var locationProvider: MutableLiveData<GpsMyLocationProvider> = MutableLiveData(null)

    fun getLocationProviderValue(): LiveData<GpsMyLocationProvider> = locationProvider
    fun configureMap() {
        mainInteractor.configureMap()
    }

    fun saveIsNeedShowDialog(isNeed: Boolean) {
        settingsInteractor.saveIsNeedDialogShow(isNeed)
    }

    fun getProvider() {
        locationProvider.postValue(mainInteractor.getLocationProvider().provider)
    }
}