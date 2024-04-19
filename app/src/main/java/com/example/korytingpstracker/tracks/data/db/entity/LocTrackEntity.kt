package com.example.korytingpstracker.tracks.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.korytingpstracker.util.getDate
import java.time.LocalDateTime
import java.time.ZoneOffset

@Entity(tableName = "Locations_tracks")
data class LocTrackEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "trackId")
    val id: Long,
    val date: String = getDate(),
    val time: String,
    val trackName: String,
    val averageSpeed: String,
    val distance: String,
    val locationPointsList: String
)