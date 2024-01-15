package com.example.korytingpstracker.tracks.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.korytingpstracker.R
import com.example.korytingpstracker.databinding.FragmentCurrentTrackBinding
import com.example.korytingpstracker.databinding.FragmentMainBinding

class CurrentTrackFragment : Fragment() {
    private var _binding: FragmentCurrentTrackBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrentTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            CurrentTrackFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}