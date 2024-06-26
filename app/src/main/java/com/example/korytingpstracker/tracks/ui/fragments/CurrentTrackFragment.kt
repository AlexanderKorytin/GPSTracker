package com.example.korytingpstracker.tracks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.korytingpstracker.R
import com.example.korytingpstracker.databinding.FragmentCurrentTrackBinding
import com.example.korytingpstracker.main_menu.ui.models.LocationTrack
import com.example.korytingpstracker.main_menu.ui.models.MainMenuScreenState
import com.example.korytingpstracker.tracks.ui.viewmodel.LocationTrackViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

class CurrentTrackFragment : Fragment() {
    private var polyLine: Polyline? = null
    private var color = 0
    private var _binding: FragmentCurrentTrackBinding? = null
    private val binding get() = _binding!!
    private val currentTrackViewModel: LocationTrackViewModel by viewModel<LocationTrackViewModel>()
    private var startPoint: GeoPoint = GeoPoint(0.0, 0.0)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        currentTrackViewModel.configureMap()
        _binding = FragmentCurrentTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val trackId = arguments?.getLong(CLICKED_TRACK) ?: 0L
        currentTrackViewModel.getTrackById(trackId)
        currentTrackViewModel.currentTrackScreenState.observe(viewLifecycleOwner) {
            when (it) {
                is MainMenuScreenState.Content -> {
                    processingResult(it.data)
                }
            }
        }
        binding.buttonMyPosition.setOnClickListener {
            binding.tvMap.controller.animateTo(startPoint)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun processingResult(currentTrack: LocationTrack) = with(binding) {
        val distance = "${tvDistance.text.split(':')[0]}: ${currentTrack.distance}"
        val averageSpeed = "${tvAverageSpeed.text.split(':')[0]}: ${currentTrack.averageSpeed} km/h"
        val name = currentTrack.locName
        val dateTime = "${tvDate.text.split(':')[0]}: ${currentTrack.date}"
        tvDate.text = dateTime
        tvDistance.text = distance
        tvAverageSpeed.text = averageSpeed
        tvName.text = name
        if (currentTrack.geoPointList.isNotEmpty()) {
            polyLine = paintedPoints(currentTrack.geoPointList)
            color = currentTrackViewModel.getColorLine()
            polyLine?.outlinePaint?.color = color
            tvMap.overlays.add(polyLine)
            startPoint = currentTrack.geoPointList[0]
            showTrack(
                currentTrack.geoPointList[0],
                currentTrack.geoPointList.let { it[it.size - 1] })
        }
        setMarkers(currentTrack.geoPointList)
    }

    private fun paintedPoints(list: List<GeoPoint>): Polyline = Polyline().apply {
        list.forEach {
            this.addPoint(it)
        }
    }

    private fun showTrack(startPosition: GeoPoint, endPoint: GeoPoint) =
        with(binding.tvMap.controller) {
            lifecycleScope.launch {
                val zoom = launch {
                    zoomTo(17.0)
                    delay(TIME_OUT)
                }
                zoom.join()
                val showStart = launch {
                    animateTo(startPosition)
                    delay(TIME_OUT)
                }
                showStart.join()
                val showEnd = launch {
                    animateTo(endPoint)
                }
            }
        }

    private fun setMarkers(list: List<GeoPoint>) = with(binding) {
        val startMarker = Marker(tvMap)
        val endMarker = Marker(tvMap)
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        startMarker.icon = resources.getDrawable(R.drawable.ic_start)
        endMarker.icon = resources.getDrawable(R.drawable.ic_finish)
        startMarker.position = list[0]
        endMarker.position = list.last()
        tvMap.overlays.add(startMarker)
        tvMap.overlays.add(endMarker)
    }

    companion object {
        private const val TIME_OUT = 2000L
        private const val CLICKED_TRACK = "clicked track"
        fun newInstance(trackId: Long?) = bundleOf(CLICKED_TRACK to trackId)

    }
}