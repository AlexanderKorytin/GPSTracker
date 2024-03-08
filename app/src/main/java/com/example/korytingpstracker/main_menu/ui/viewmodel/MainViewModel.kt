package com.example.korytingpstracker.main_menu.ui.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.korytingpstracker.main_menu.data.dto.LocationDto
import com.example.korytingpstracker.main_menu.data.service.LocationService
import com.example.korytingpstracker.main_menu.domain.api.MainInteractor
import com.example.korytingpstracker.main_menu.ui.models.LocationTrack
import com.example.korytingpstracker.main_menu.ui.models.MainMenuScreenState
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

    private val correctionTime = 1000f
    private val correctionSpeed = 3.6f
    private var timer: Timer? = null
    private var startTime = 0L
    private var currentTime = 0L
    private var _screenState: MutableLiveData<MainMenuScreenState> = MutableLiveData()
    val screenState = _screenState
    private val _currentTime: MutableLiveData<String> = MutableLiveData()
    fun getColorLine(): Int {
        return settingsInteractor.getColorLocationTrackLine()
    }

    fun getCurrentTime(): LiveData<String> = _currentTime

    private var locationProvider: MutableLiveData<GpsMyLocationProvider> = MutableLiveData(null)
    fun getLocationProviderValue(): LiveData<GpsMyLocationProvider> = locationProvider

    private var stateLocService: MutableLiveData<Boolean> = MutableLiveData(false)

    fun getStateService(): LiveData<Boolean> = stateLocService

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == LocationService.LOC_INTENT) {
                val locData = intent.getSerializableExtra(LocationService.LOC_INTENT) as LocationDto
                _screenState.postValue(
                    MainMenuScreenState.Content(
                        LocationTrack(
                            speed = String.format("%.1f", correctionSpeed * locData.speed),
                            distance = String.format("%.1f", locData.distance),
                            averageSpeed = getAverageSpeed(distance = locData.distance),
                            geoPointList = locData.geoPointList,
                        )
                    )
                )
            }
        }
    }

    private fun getAverageSpeed(distance: Float): String {
        return String.format(
            "%.1f",
            correctionSpeed * (distance / ((System.currentTimeMillis() - startTime) / correctionTime))
        )
    }

    // очищаем данные геолокации данного сеанса работы сервиса
    fun clearLocData() {
        _screenState.postValue(MainMenuScreenState.Content(LocationTrack()))
    }

    fun registeringRecevier(context: Context) {
        LocalBroadcastManager.getInstance(context)
            .registerReceiver(receiver, IntentFilter(LocationService.LOC_INTENT))
    }

    fun unRegisteringRecevier(context: Context) {
        LocalBroadcastManager.getInstance(context)
            .unregisterReceiver(receiver)
    }

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
                currentTime = System.currentTimeMillis() - startTime
                viewModelScope.launch(Dispatchers.Main) {
                    _currentTime.value = getTime(currentTime)
                }
            }

        }, 1000L, 1000L)
    }

    fun stopTimer() {
        timer?.cancel()
    }
}