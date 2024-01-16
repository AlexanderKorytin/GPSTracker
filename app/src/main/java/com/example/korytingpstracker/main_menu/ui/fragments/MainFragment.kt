package com.example.korytingpstracker.main_menu.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.korytingpstracker.databinding.FragmentMainBinding
import com.example.korytingpstracker.main_menu.ui.viewmodel.MainViewModel
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by viewModel<MainViewModel>()
    private val requester = PermissionRequester.instance()
    private lateinit var myLocationNewOverlay: MyLocationNewOverlay
    private lateinit var myGPSProvider: GpsMyLocationProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainViewModel.configureMap()
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val anim = AnimationUtils.loadAnimation(
            requireContext(),
            com.google.android.material.R.anim.abc_fade_in
        )
        binding.root.startAnimation(anim)
        mainViewModel.getProvider()
        initOsm()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val anim = AnimationUtils.loadAnimation(
            requireContext(),
            com.google.android.material.R.anim.abc_fade_out
        )
        binding.root.startAnimation(anim)
        _binding = null
    }

    private fun initOsm() = with(binding) {
        lifecycleScope.launch(Dispatchers.Main) {
            mainViewModel.getLocationProviderValue().observe(viewLifecycleOwner) {
                myGPSProvider = it
            }
            myLocationNewOverlay = MyLocationNewOverlay(myGPSProvider, map)
            checkPermissionLocation()
            myLocationNewOverlay.runOnFirstFix {
                binding.map.overlays.clear()
                binding.map.overlays.add(myLocationNewOverlay)
            }
            myLocationNewOverlay.enableFollowLocation()
            map.controller.setZoom(17.0)
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun checkPermissionLocation() {
        requester.request(
            Manifest.permission.ACCESS_FINE_LOCATION
        ).collect { result ->
            when (result) {
                // Пользователь дал разрешение, можно продолжать работу
                is PermissionResult.Granted -> {
                    myLocationNewOverlay.enableMyLocation()
                    myLocationNewOverlay.enableFollowLocation()
                }
                //Пользователь отказал в предоставлении разрешения
                is PermissionResult.Denied -> {}
                // Запрещено навсегда, перезапрашивать нет смысла, предлагаем пройти в настройки
                is PermissionResult.Denied.DeniedPermanently -> {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.data = Uri.fromParts("package", requireActivity().packageName, null)
                    requireActivity().startActivity(intent)
                }
                // Запрещено навсегда, перезапрашивать нет смысла, предлагаем пройти в настройки
                is PermissionResult.Cancelled -> {
                    return@collect
                }
            }
        }
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}