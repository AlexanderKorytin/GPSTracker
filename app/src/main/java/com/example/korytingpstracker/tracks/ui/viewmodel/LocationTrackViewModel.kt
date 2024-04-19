package com.example.korytingpstracker.tracks.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.korytingpstracker.main_menu.domain.api.MainInteractor
import com.example.korytingpstracker.main_menu.ui.models.LocationTrack
import com.example.korytingpstracker.main_menu.ui.models.MainMenuScreenState
import com.example.korytingpstracker.settings.ui.domain.api.SettingsInteractor
import com.example.korytingpstracker.tracks.domain.api.LocationTrackInteractor
import com.example.korytingpstracker.tracks.ui.models.LocationTracksScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationTrackViewModel(
    private val dataBaseInteractor: LocationTrackInteractor,
    private val settingsInteractor: SettingsInteractor,
    private val mapInteractor: MainInteractor
) : ViewModel() {
    private var _listTracksScreenState = MutableLiveData<LocationTracksScreenState>()
    val listTracksScreenState get() = _listTracksScreenState

    private var _currentTrackScreenState = MutableLiveData<MainMenuScreenState>()
    val currentTrackScreenState get() = _currentTrackScreenState

    fun getAllLocTracks() {
        viewModelScope.launch(Dispatchers.IO) {
            dataBaseInteractor.getAllLocTracks().collect {
                if (it.isEmpty()) {
                    _listTracksScreenState.postValue(LocationTracksScreenState.Empty)
                } else {
                    _listTracksScreenState.postValue(LocationTracksScreenState.Content(it))
                }
            }
        }
    }

    fun deleteCurrentTrack(track: LocationTrack) {
        viewModelScope.launch(Dispatchers.IO) {
            dataBaseInteractor.deleteLocTrack(track)
            getAllLocTracks()
        }
    }

    fun getTrackById(trackId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dataBaseInteractor.getLocTrackById(trackId).collect {
                _currentTrackScreenState.postValue(MainMenuScreenState.Content(it))
            }
        }
    }

    fun configureMap(){
        mapInteractor.configureMap()
    }

    fun getColorLine(): Int {
        return settingsInteractor.getColorLocationTrackLine()
    }
}