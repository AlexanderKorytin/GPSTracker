package com.example.korytingpstracker.tracks.data.convertor

import com.example.korytingpstracker.main_menu.ui.models.LocationTrack
import com.example.korytingpstracker.tracks.data.entity.LocTrackEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.osmdroid.util.GeoPoint

fun map(locTrackEntity: LocTrackEntity): LocationTrack {
    return LocationTrack(
        locName = locTrackEntity.trackName,
        averageSpeed = locTrackEntity.averageSpeed,
        distance = locTrackEntity.distance,
        geoPointList = Gson().fromJson(
            locTrackEntity.locationPointsList,
            object : TypeToken<List<GeoPoint>>() {}.type
        )
    )
}

fun map(locTrack: LocationTrack): LocTrackEntity {
    return LocTrackEntity(
        trackName = locTrack.locName,
        distance = locTrack.distance,
        averageSpeed = locTrack.averageSpeed,
        locationPointsList = Gson().toJson(locTrack.geoPointList)
    )
}