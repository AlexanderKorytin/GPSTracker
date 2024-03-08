package com.example.korytingpstracker.tracks.domain.impl

import com.example.korytingpstracker.main_menu.ui.models.LocationTrack
import com.example.korytingpstracker.tracks.domain.api.LocationTrackInteractor
import com.example.korytingpstracker.tracks.domain.api.LocationTrackRepository
import kotlinx.coroutines.flow.Flow

class LocTrackInteractorImpl(private val locRepository: LocationTrackRepository) :
    LocationTrackInteractor {
    override suspend fun saveLocationTrack(locTrack: LocationTrack) {
        locRepository.saveLocationTrack(locTrack)
    }

    override suspend fun getAllLocTracks(): Flow<List<LocationTrack>> {
        return locRepository.getAllLocTracks()
    }

    override suspend fun getLocTrackByName(trackName: String): Flow<LocationTrack> {
        return locRepository.getLocTrackByName(trackName)
    }

    override suspend fun deleteLocTrack(locTrack: LocationTrack) {
        locRepository.deleteLocTrack(locTrack)
    }
}