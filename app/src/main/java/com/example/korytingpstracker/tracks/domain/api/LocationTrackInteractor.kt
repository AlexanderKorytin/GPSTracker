package com.example.korytingpstracker.tracks.domain.api

import com.example.korytingpstracker.main_menu.ui.models.LocationTrack
import kotlinx.coroutines.flow.Flow

interface LocationTrackInteractor {
    suspend fun saveLocationTrack(locTrack: LocationTrack)


    suspend fun getAllLocTracks(): Flow<List<LocationTrack>>


    suspend fun getLocTrackByName(trackName: String): Flow<LocationTrack>


    suspend fun deleteLocTrack(locTrack: LocationTrack)
}