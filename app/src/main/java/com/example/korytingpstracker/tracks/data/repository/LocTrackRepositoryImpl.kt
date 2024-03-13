package com.example.korytingpstracker.tracks.data.repository

import com.example.korytingpstracker.core.ui.db.AppDataBase
import com.example.korytingpstracker.main_menu.ui.models.LocationTrack
import com.example.korytingpstracker.tracks.data.db.convertor.map
import com.example.korytingpstracker.tracks.domain.api.LocationTrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocTrackRepositoryImpl(private val db: AppDataBase) : LocationTrackRepository {
    override suspend fun saveLocationTrack(locTrack: LocationTrack) {
        db.getLocTrackDao().saveLocationTrack(map(locTrack))
    }

    override suspend fun getAllLocTracks(): Flow<List<LocationTrack>> = flow {
        emit(db.getLocTrackDao().getAllLocTracks().sortedBy { it.id }.map { map(it) })
    }

    override suspend fun getLocTrackByName(trackName: String): Flow<LocationTrack> = flow {
        emit(map(db.getLocTrackDao().getLocTrackByName(trackName)))
    }

    override suspend fun deleteLocTrack(locTrack: LocationTrack) {
        db.getLocTrackDao().deleteLocTrack(map(locTrack))
    }
}