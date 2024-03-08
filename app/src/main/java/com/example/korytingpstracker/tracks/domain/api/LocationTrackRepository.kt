package com.example.korytingpstracker.tracks.domain.api

import com.example.korytingpstracker.main_menu.ui.models.LocationTrack
import com.example.korytingpstracker.tracks.data.entity.LocTrackEntity
import kotlinx.coroutines.flow.Flow

interface LocationTrackRepository {

    suspend fun saveLocationTrack(locTrack: LocationTrack)


    suspend fun getAllLocTracks(): Flow<List<LocationTrack>>


    suspend fun getLocTrackByName(trackName: String): Flow<LocationTrack>


    suspend fun deleteLocTrack(locTrack: LocationTrack)
}