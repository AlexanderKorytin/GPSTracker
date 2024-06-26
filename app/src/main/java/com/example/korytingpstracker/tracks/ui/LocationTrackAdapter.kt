package com.example.korytingpstracker.tracks.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.korytingpstracker.databinding.LocationTrackItemBinding
import com.example.korytingpstracker.main_menu.ui.models.LocationTrack

class LocationTrackAdapter(
    private val onDeleteClick: (LocationTrack) -> Unit,
    private val onItemClick: (LocationTrack) -> Unit
) :
    ListAdapter<LocationTrack, LocationTrackViewHolder>(LocationTrackDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationTrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        val binding = LocationTrackItemBinding.inflate(layoutInspector, parent, false)
        return LocationTrackViewHolder(binding, onDeleteClick, onItemClick)
    }

    override fun onBindViewHolder(holder: LocationTrackViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}