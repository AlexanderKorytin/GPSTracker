package com.example.korytingpstracker.tracks.data.db.convertor

import com.example.korytingpstracker.main_menu.ui.models.LocationTrack
import com.example.korytingpstracker.tracks.data.db.entity.LocTrackEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.osmdroid.util.GeoPoint
import java.time.LocalDateTime
import java.time.ZoneOffset

fun map(locTrackEntity: LocTrackEntity): LocationTrack {
    return LocationTrack(
        id = locTrackEntity.id,
        locName = locTrackEntity.trackName,
        averageSpeed = locTrackEntity.averageSpeed,
        distance = locTrackEntity.distance,
        time = locTrackEntity.time,
        date = locTrackEntity.date,
        geoPointList = Gson().fromJson(
            locTrackEntity.locationPointsList,
            object : TypeToken<List<GeoPoint>>() {}.type
        )
    )
}

fun map(locTrack: LocationTrack): LocTrackEntity {
    return LocTrackEntity(
        id = locTrack.id,
        trackName = locTrack.locName.ifEmpty {
           "Empty Name"
        },
        time = locTrack.time,
        distance = locTrack.distance,
        averageSpeed = locTrack.averageSpeed,
        locationPointsList = Gson().toJson(locTrack.geoPointList)
    )
}