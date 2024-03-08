package com.example.korytingpstracker.tracks.ui

import androidx.recyclerview.widget.DiffUtil
import com.example.korytingpstracker.main_menu.ui.models.LocationTrack

class LocationTrackDiffUtil(): DiffUtil.ItemCallback<LocationTrack>() {
    override fun areItemsTheSame(oldItem: LocationTrack, newItem: LocationTrack): Boolean {
        return oldItem.locName == newItem.locName
    }

    override fun areContentsTheSame(oldItem: LocationTrack, newItem: LocationTrack): Boolean {
        return oldItem.locName == newItem.locName &&
                oldItem.distance == newItem.distance &&
                oldItem.averageSpeed == newItem.averageSpeed
    }
}