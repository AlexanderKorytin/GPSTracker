package com.example.korytingpstracker.tracks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.korytingpstracker.R
import com.example.korytingpstracker.databinding.FragmentTracksListBinding
import com.example.korytingpstracker.main_menu.ui.models.LocationTrack
import com.example.korytingpstracker.tracks.ui.LocationTrackAdapter
import com.example.korytingpstracker.tracks.ui.models.LocationTrackScreenState
import com.example.korytingpstracker.tracks.ui.viewmodel.LocationTrackViewModel
import com.example.korytingpstracker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class TracksListFragment : Fragment() {
    private var _binding: FragmentTracksListBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<LocationTrackViewModel>()
    private var adapter: LocationTrackAdapter? = LocationTrackAdapter(
        { locTrack ->
            deleteTrack?.let { delete -> delete(locTrack) }
        },
        { locTrack ->
            clickTrack?.let { clickDebounce -> clickDebounce(locTrack) }
        }
    )
    private var deleteTrack: ((track: LocationTrack) -> Unit)? = null
    private var clickTrack: ((track: LocationTrack) -> Unit)? = null

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
            setOnTrackClickListener()
            deleteTrack = { viewModel.deleteCurrentTrack(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val anim = AnimationUtils.loadAnimation(
            requireContext(),
            com.google.android.material.R.anim.abc_fade_out
        )
        binding.root.startAnimation(anim)
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

    private fun setOnTrackClickListener() {
        clickTrack = debounce(
            CLICK_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            false
        ) {
            // val vacancyBundle = bundleOf(VACANCY_ID to vacancyItem.id)
            findNavController().navigate(R.id.action_tracksListFragment_to_currentTrackFragment)
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 300L
    }
}