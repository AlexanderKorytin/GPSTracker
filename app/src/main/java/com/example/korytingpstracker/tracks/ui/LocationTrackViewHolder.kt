package com.example.korytingpstracker.tracks.ui

import androidx.recyclerview.widget.RecyclerView
import com.example.korytingpstracker.databinding.LocationTrackItemBinding
import com.example.korytingpstracker.main_menu.ui.models.LocationTrack

class LocationTrackViewHolder(
    private val binding: LocationTrackItemBinding,
    private val clickListner:(LocationTrack) -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(locTrack: LocationTrack) = with(binding) {
        tvTrackDistance.setText("Distance: ${locTrack.distance} m")
        tvTrackName.setText("Track name: ${locTrack.locName}")
        tvTrackDate.setText("Date: ${locTrack.date}")
        tvTrackTime.setText("Track time: ${locTrack.time}")
        botDelete.setOnClickListener {
            clickListner(locTrack)
        }
    }
}