package com.example.korytingpstracker.tracks.ui

import androidx.recyclerview.widget.RecyclerView
import com.example.korytingpstracker.databinding.LocationTrackItemBinding
import com.example.korytingpstracker.main_menu.ui.models.LocationTrack

class LocationTrackViewHolder(private val binding: LocationTrackItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(locTrack: LocationTrack) = with(binding){
        tvTrackDistance.text = "Distance: ${locTrack.distance} m"
        tvTrackName.text = "Track name: ${locTrack.locName}"
    }
}