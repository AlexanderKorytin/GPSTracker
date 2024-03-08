package com.example.korytingpstracker.tracks.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.ZoneOffset

@Entity(tableName = "Locations_tracks")
data class LocTrackEntity(
    @PrimaryKey() @ColumnInfo(name = "trackId")
    val id: Long = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
    val trackName: String,
    val averageSpeed: String,
    val distance: String,
    val locationPointsList: String
)