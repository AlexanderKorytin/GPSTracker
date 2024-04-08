package com.example.korytingpstracker.tracks.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.korytingpstracker.main_menu.ui.models.LocationTrack
import com.example.korytingpstracker.tracks.domain.api.LocationTrackInteractor
import com.example.korytingpstracker.tracks.ui.models.LocationTrackScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationTrackViewModel(private val interactor: LocationTrackInteractor) : ViewModel() {
    private var _screenState = MutableLiveData<LocationTrackScreenState>()
    val screenState = _screenState

    fun getAllLocTracks() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getAllLocTracks().collect {
                if (it.isEmpty()) {
                    _screenState.postValue(LocationTrackScreenState.Empty)
                } else {
                    _screenState.postValue(LocationTrackScreenState.Content(it))
                }
            }
        }
    }

    fun deleteCurrentTrack(track: LocationTrack) {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.deleteLocTrack(track)
            getAllLocTracks()
        }
    }
}