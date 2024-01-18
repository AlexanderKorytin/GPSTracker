package com.example.korytingpstracker.main_menu.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.korytingpstracker.main_menu.domain.api.MainInteractor
import com.example.korytingpstracker.settings.ui.domain.api.SettingsInteractor
import com.example.korytingpstracker.util.getTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import java.util.Timer
import java.util.TimerTask

class MainViewModel(
    private val mainInteractor: MainInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private var timer: Timer? = null
    private var startTime = 0L

    private val currentTime: MutableLiveData<String> = MutableLiveData()
    fun getCurrentTime(): LiveData<String> = currentTime

    private var locationProvider: MutableLiveData<GpsMyLocationProvider> = MutableLiveData(null)
    fun getLocationProviderValue(): LiveData<GpsMyLocationProvider> = locationProvider

    private var stateLocService: MutableLiveData<Boolean> = MutableLiveData(false)

    fun getStateService(): LiveData<Boolean> = stateLocService

    fun configureMap() {
        mainInteractor.configureMap()
    }

    fun checkedLocationServiceState() {
        stateLocService.value = (mainInteractor.getStateSeervice())
    }

    fun saveIsNeedShowDialog(isNeed: Boolean) {
        settingsInteractor.saveIsNeedDialogShow(isNeed)
    }

    fun getProvider() {
        locationProvider.postValue(mainInteractor.getLocationProvider().provider)
    }

    fun setStartTime() {
        mainInteractor.setStartTime(System.currentTimeMillis())
    }

    fun startTimer() {
        timer?.cancel()
        startTime = mainInteractor.getStartTime()
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                viewModelScope.launch(Dispatchers.Main) {
                    currentTime.value = getTime(System.currentTimeMillis() - startTime)
                }
            }

        }, 1000L, 1000L)
    }

    fun stopTimer() {
        timer?.cancel()
    }
}