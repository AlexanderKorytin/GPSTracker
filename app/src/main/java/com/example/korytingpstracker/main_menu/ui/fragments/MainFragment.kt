package com.example.korytingpstracker.main_menu.ui.fragments

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.korytingpstracker.R
import com.example.korytingpstracker.app.App
import com.example.korytingpstracker.databinding.FragmentMainBinding
import com.example.korytingpstracker.main_menu.data.service.LocationService
import com.example.korytingpstracker.main_menu.ui.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MainFragment : Fragment() {
    private var isServiceLocRunning = false
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by viewModel<MainViewModel>()
    private val requester = PermissionRequester.instance()
    private lateinit var myLocationNewOverlay: MyLocationNewOverlay
    private lateinit var myGPSProvider: GpsMyLocationProvider
    private var fineResult: PermissionResult? = null
    private var backResult: PermissionResult? = null
    private var isFineLocDialogShowed = false
    private var isBackLocDialogShowed = false

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
        setOnClicks()
        checkLocationServiseState()
        binding.root.startAnimation(anim)
        mainViewModel.getProvider()
        updateTime()
        checkPermission()
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
            myLocationNewOverlay.enableMyLocation()
            myLocationNewOverlay.enableFollowLocation()
            myLocationNewOverlay.runOnFirstFix {
                map.overlays.clear()
                map.overlays.add(myLocationNewOverlay)
            }
            myLocationNewOverlay.enableFollowLocation()
            map.controller.setZoom(17.0)
        }
    }

    private fun checkPermission() {
        val arrayPermissionResult = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            arrayPermissionResult.clear()
            arrayPermissionResult.addAll(
                arrayOf(
                    ACCESS_FINE_LOCATION,
                    ACCESS_BACKGROUND_LOCATION,
                )
            )
        } else {
            arrayPermissionResult.clear()
            arrayPermissionResult.addAll(
                arrayOf(
                    ACCESS_FINE_LOCATION,
                )
            )
        }
        lifecycleScope.launch {
            checkPermissionLocation(arrayPermissionResult.toTypedArray())
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun checkPermissionLocation(listPermission: Array<String>) {
        listPermission.forEach { permission ->
            when (permission) {
                ACCESS_FINE_LOCATION -> {
                    requester.request(permission).collect { result ->
                        fineResult = result
                        getResultFineLocation(result)
                    }
                }

                ACCESS_BACKGROUND_LOCATION -> {
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                        requester.request(permission).collect { result ->
                            backResult = result
                            getResultBackGroundLocation(result)
                        }
                    }
                    if (
                        (Build.VERSION.SDK_INT != Build.VERSION_CODES.Q)
                        && fineResult is PermissionResult.Granted
                        && backResult !is PermissionResult.Granted
                    ) {
                        val dialog = MaterialAlertDialogBuilder(requireContext())
                            .setCancelable(false)
                            .setTitle(requireContext().getString(R.string.dialog_back_loc_title))
                            .setMessage(requireContext().getString(R.string.dialog_back_loc_message))
                            .setNeutralButton(requireContext().getString(R.string.dialog_back_loc_neutral)) { _, _ ->
                                isBackLocDialogShowed = false
                                lifecycleScope.launch(Dispatchers.IO) {
                                    requester.request(permission).collect { result ->
                                        backResult = result
                                        getResultBackGroundLocation(result)
                                    }
                                }
                            }
                        if (!isBackLocDialogShowed) {
                            isBackLocDialogShowed = true
                            dialog.show()
                        }
                    }
                }
            }
        }
    }

    private suspend fun getResultFineLocation(result: PermissionResult) {
        when (result) {
            // Пользователь дал разрешение, можно продолжать работу
            is PermissionResult.Granted -> {
                initOsm()
            }
            //Пользователь отказал в предоставлении разрешения
            is PermissionResult.Denied -> {
                val dialog = MaterialAlertDialogBuilder(requireContext())
                    .setCancelable(false)
                    .setTitle(requireContext().getString(R.string.dialog_location_title))
                    .setMessage(requireContext().getString(R.string.dialog_location_message))
                    .setPositiveButton(requireContext().getString(R.string.yes)) { _, _ ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.data =
                            Uri.fromParts("package", requireActivity().packageName, null)
                        requireActivity().startActivity(intent)
                    }
                    .setNegativeButton(requireContext().getString(R.string.no)) { _, _ ->
                        requireActivity().finish()
                    }.show()
            }
            // Canceled
            is PermissionResult.Cancelled -> {
                return
            }
        }
    }

    private suspend fun getResultBackGroundLocation(result: PermissionResult) {
        when (result) {
            // Пользователь дал разрешение, можно продолжать работу
            is PermissionResult.Granted -> {
                App.needBackGroundLocPerm = false
                mainViewModel.saveIsNeedShowDialog(App.needBackGroundLocPerm)

            }
            //Пользователь отказал в предоставлении разрешения
            is PermissionResult.Denied.NeedsRationale -> {

            }
            // Запрещено навсегда, перезапрашивать нет смысла, предлагаем пройти в настройки
            is PermissionResult.Denied.DeniedPermanently -> {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.data = Uri.fromParts("package", requireActivity().packageName, null)
                requireActivity().startActivity(intent)
            }
            // Canceled
            is PermissionResult.Cancelled -> {
                return
            }
        }
    }


    private fun startLocService() {
        requireContext().startForegroundService(
            Intent(
                requireContext(),
                LocationService::class.java
            )
        )
    }

    private fun updateTime() {
        mainViewModel.getCurrentTime().observe(viewLifecycleOwner) {
            binding.time.text = "${binding.time.text.split(':')[0]}: ${it}"
        }
    }

    private fun startStopServise() {
        if (!isServiceLocRunning) {
            startLocService()
            mainViewModel.setStartTime()
            mainViewModel.startTimer()
            binding.startStop.setImageResource(R.drawable.ic_stop)
        } else {
            activity?.stopService(Intent(activity, LocationService::class.java))
            mainViewModel.stopTimer()
            binding.startStop.setImageResource(R.drawable.ic_play)
        }
        isServiceLocRunning = !isServiceLocRunning
    }

    private fun checkLocationServiseState() {
        mainViewModel.checkedLocationServiceState()
        mainViewModel.getStateService().observe(viewLifecycleOwner) {
            isServiceLocRunning = it
            if (isServiceLocRunning) {
                binding.startStop.setImageResource(R.drawable.ic_stop)
                mainViewModel.startTimer()
            } else {
                binding.startStop.setImageResource(R.drawable.ic_play)
            }
        }
    }

    fun setOnClicks() = with(binding) {
        val listner = onClick()
        startStop.setOnClickListener(listner)
    }

    private fun onClick(): OnClickListener {
        return OnClickListener {
            when (it.id) {
                R.id.start_stop -> {
                    startStopServise()
                }
            }
        }
    }

}