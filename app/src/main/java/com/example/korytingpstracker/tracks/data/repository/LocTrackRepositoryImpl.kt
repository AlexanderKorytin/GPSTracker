package com.example.korytingpstracker.tracks.data.repository

import android.content.Context
import android.widget.Toast
import com.example.korytingpstracker.R
import com.example.korytingpstracker.core.ui.db.AppDataBase
import com.example.korytingpstracker.main_menu.ui.models.LocationTrack
import com.example.korytingpstracker.tracks.data.db.convertor.map
import com.example.korytingpstracker.tracks.domain.api.LocationTrackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class LocTrackRepositoryImpl(
    private val appDataBase: AppDataBase,
    private val context: Context
) : LocationTrackRepository {
    override suspend fun saveLocationTrack(locTrack: LocationTrack) {
        appDataBase.getLocTrackDao().saveLocationTrack(map(locTrack))
    }

    override suspend fun getAllLocTracks(): Flow<List<LocationTrack>> = flow {
        emit(appDataBase.getLocTrackDao().getAllLocTracks().sortedBy { it.id }.map { map(it) })
    }

    override suspend fun getLocTrackByName(trackName: String): Flow<LocationTrack> = flow {
        emit(map(appDataBase.getLocTrackDao().getLocTrackByName(trackName)))
    }

    override suspend fun getLocTrackById(trackId: Long): Flow<LocationTrack> = flow {
        val locTrack = appDataBase.getLocTrackDao().getLocTrackById(trackId)
        try {
            emit(map(locTrack))
        } catch (e: NullPointerException) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    context.getString(R.string.app_data_base_error),
                    Toast.LENGTH_LONG
                ).show()
            }
            emit(LocationTrack())
        }
    }

    override suspend fun deleteLocTrack(locTrack: LocationTrack) {
        appDataBase.getLocTrackDao().deleteLocTrack(map(locTrack))
    }
}