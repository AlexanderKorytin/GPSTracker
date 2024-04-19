package com.example.korytingpstracker.tracks.ui.models

import com.example.korytingpstracker.main_menu.ui.models.LocationTrack

sealed interface LocationTracksScreenState {
    data object Empty: LocationTracksScreenState
    data class Content(val data: List<LocationTrack>): LocationTracksScreenState
}