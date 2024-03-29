package com.example.korytingpstracker.tracks.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.korytingpstracker.tracks.data.db.entity.LocTrackEntity

@Dao
interface LocationsTracksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLocationTrack(locTrack: LocTrackEntity)

    @Query("SELECT * FROM Locations_tracks")
    suspend fun getAllLocTracks(): List<LocTrackEntity>

    @Query("SELECT * FROM Locations_tracks WHERE trackName = :trackName")
    suspend fun getLocTrackByName(trackName: String): LocTrackEntity

    @Delete(LocTrackEntity::class)
    suspend fun deleteLocTrack(locTrack: LocTrackEntity)
}