package com.example.korytingpstracker.tracks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.korytingpstracker.databinding.FragmentTracksListBinding
import com.example.korytingpstracker.main_menu.ui.models.LocationTrack
import com.example.korytingpstracker.tracks.ui.LocationTrackAdapter
import com.example.korytingpstracker.tracks.ui.models.LocationTrackScreenState
import com.example.korytingpstracker.tracks.ui.viewmodel.LocationTrackViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TracksListFragment : Fragment() {
    private var _binding: FragmentTracksListBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<LocationTrackViewModel>()
    private var adapter: LocationTrackAdapter? = LocationTrackAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTracksListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val anim = AnimationUtils.loadAnimation(
            requireContext(),
            com.google.android.material.R.anim.abc_fade_in
        )
        binding.root.startAnimation(anim)
        viewModel.getAllLocTracks()
        binding.tvListTracks.adapter = adapter
        viewModel.screenState.observe(viewLifecycleOwner) {
            when (it) {
                is LocationTrackScreenState.Empty -> {
                    showEmpty()
                }

                is LocationTrackScreenState.Content -> {
                    showContent(it.data)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val anim = AnimationUtils.loadAnimation(
            requireContext(),
            com.google.android.material.R.anim.abc_fade_out
        )
        binding.root.startAnimation(anim)
        binding.tvListTracks.adapter = null
        adapter = null
        _binding = null
    }

    private fun showEmpty() {
        binding.tvEmptyImage.isVisible = true
        binding.tvEmptyText.isVisible = true
        binding.tvListTracks.isVisible = false
    }

    private fun showContent(list: List<LocationTrack>) {
        binding.tvEmptyImage.isVisible = false
        binding.tvEmptyText.isVisible = false
        binding.tvListTracks.isVisible = true
        adapter?.submitList(list)
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            TracksListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}