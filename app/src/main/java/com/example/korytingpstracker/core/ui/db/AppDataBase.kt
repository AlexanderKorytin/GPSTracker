package com.example.korytingpstracker.core.ui.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.korytingpstracker.tracks.data.db.dao.LocationsTracksDao
import com.example.korytingpstracker.tracks.data.db.entity.LocTrackEntity

@Database(entities = [LocTrackEntity::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun getLocTrackDao(): LocationsTracksDao
}