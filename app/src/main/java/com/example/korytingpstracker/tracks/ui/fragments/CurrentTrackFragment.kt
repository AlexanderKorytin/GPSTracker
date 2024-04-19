package com.example.korytingpstracker.tracks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.korytingpstracker.databinding.FragmentCurrentTrackBinding
import com.example.korytingpstracker.main_menu.ui.models.LocationTrack
import com.example.korytingpstracker.main_menu.ui.models.MainMenuScreenState
import com.example.korytingpstracker.tracks.ui.viewmodel.LocationTrackViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.views.overlay.Polyline

class CurrentTrackFragment : Fragment() {
    private var polyLine: Polyline? = null
    private var color = 0
    private var _binding: FragmentCurrentTrackBinding? = null
    private val binding get() = _binding!!

    private val currentTrackViewModel: LocationTrackViewModel by viewModel<LocationTrackViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val trackId = arguments?.getLong(CLICKED_TRACK) ?: 0L
        currentTrackViewModel.getTrackById(trackId)
        color = currentTrackViewModel.getColorLine()
        polyLine = Polyline()
        polyLine?.outlinePaint?.color = color
        currentTrackViewModel.currentTrackScreenState.observe(viewLifecycleOwner) {
            when (it) {
                is MainMenuScreenState.Content -> {
                    processingResult(it.data)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun processingResult(currentTrack: LocationTrack) = with(binding) {
        val distnce = "${tvDistance.text.split(':')[0]}: ${currentTrack.distance}"
        val averageSpeed = "${tvAverageSpeed.text.split(':')[0]}: ${currentTrack.averageSpeed} km/h"
        val name = currentTrack.locName
        val dateTime = "${tvDate.text.split(':')[0]}: ${currentTrack.date}"
        tvDate.text = dateTime
        tvDistance.text = distnce
        tvAverageSpeed.text = averageSpeed
        tvName.text = name
    }

    companion object {
        private const val CLICKED_TRACK = "clicked track"
        fun newInstance(trackId: Long?) = bundleOf(CLICKED_TRACK to trackId)

    }
}