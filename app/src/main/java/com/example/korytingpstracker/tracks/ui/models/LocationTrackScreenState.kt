package com.example.korytingpstracker.tracks.ui.models

import com.example.korytingpstracker.main_menu.ui.models.LocationTrack

sealed interface LocationTrackScreenState {
    data object Empty: LocationTrackScreenState
    data class Content(val data: List<LocationTrack>): LocationTrackScreenState
}