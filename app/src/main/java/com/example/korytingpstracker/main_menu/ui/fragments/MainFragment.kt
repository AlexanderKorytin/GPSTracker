package com.example.korytingpstracker.main_menu.ui.fragments

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
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
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.korytingpstracker.R
import com.example.korytingpstracker.app.App
import com.example.korytingpstracker.databinding.FragmentMainBinding
import com.example.korytingpstracker.main_menu.data.service.LocationService
import com.example.korytingpstracker.main_menu.ui.models.LocationTrack
import com.example.korytingpstracker.main_menu.ui.models.MainMenuScreenState
import com.example.korytingpstracker.main_menu.ui.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MainFragment : Fragment() {
    private var polyLine: Polyline? = null
    private var isServiceLocRunning = false
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by viewModel<MainViewModel>()
    private val requester = PermissionRequester.instance()
    private lateinit var myLocationNewOverlay: MyLocationNewOverlay
    private lateinit var myGPSProvider: GpsMyLocationProvider
    private var fineResult: PermissionResult? = null
    private var backResult: PermissionResult? = null
    private var isBackLocDialogShowed = false
    private val pointsList = mutableListOf<GeoPoint>()
    private var color = 0
    private var locTrackForSaved = LocationTrack()

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
        color = mainViewModel.getColorLine()
        setOnClicks()
        checkLocationServiseState()
        binding.root.startAnimation(anim)
        mainViewModel.getProvider()
        updateTime()
        checkPermission()
        mainViewModel.registeringRecevier(requireContext())
        polyLine = Polyline()
        polyLine?.outlinePaint?.color = color
        mainViewModel.screenState.observe(viewLifecycleOwner) {
            when (it) {
                is MainMenuScreenState.Content -> {
                    processingResult(it.data)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkPermission()
        polyLine?.outlinePaint?.color = color
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val anim = AnimationUtils.loadAnimation(
            requireContext(),
            com.google.android.material.R.anim.abc_fade_out
        )
        binding.root.startAnimation(anim)
        mainViewModel.unRegisteringRecevier(requireContext())
        pointsList.clear()
        _binding = null
    }

    private fun initOsm() = with(binding) {
        lifecycleScope.launch(Dispatchers.Main) {
            mainViewModel.getLocationProviderValue().observe(viewLifecycleOwner) {
                myGPSProvider = it
            }
            myLocationNewOverlay = MyLocationNewOverlay(myGPSProvider, tvMap)
            myLocationNewOverlay.enableMyLocation()
            myLocationNewOverlay.enableFollowLocation()
            myLocationNewOverlay.runOnFirstFix {
                tvMap.overlays.clear()
                tvMap.overlays.add(myLocationNewOverlay)
                tvMap.overlays.add(polyLine)
            }
            myLocationNewOverlay.enableFollowLocation()
            tvMap.controller.setZoom(17.0)
        }
    }

    fun showMyLocation() = with(binding) {

    }

    private fun checkPermission() {
        val arrayPermissionResult = mutableListOf<String>()
        arrayPermissionResult.clear()
        arrayPermissionResult.addAll(
            arrayOf(
                ACCESS_FINE_LOCATION,
                ACCESS_BACKGROUND_LOCATION,
            )
        )
        lifecycleScope.launch(Dispatchers.Main + SupervisorJob()) {
            val defer = lifecycleScope.async {
                checkPermissionLocation(arrayPermissionResult.toTypedArray())
            }
            try {
                defer.await()
            } catch (e: IllegalStateException) {
                Toast.makeText(requireContext(), "", Toast.LENGTH_LONG).show()
                requireActivity().finish()
            }
        }
    }

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
                                lifecycleScope.launch {
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

    private fun getResultFineLocation(result: PermissionResult) {
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

    private fun getResultBackGroundLocation(result: PermissionResult) {
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
            binding.tvTime.text = "${binding.tvTime.text.split(':')[0]}: ${it}"
        }
    }

    private fun startStopService() {
        if (!isServiceLocRunning) {
            startLocService()
            mainViewModel.setStartTime()
            mainViewModel.startTimer()
            binding.buttonStartStop.setImageResource(R.drawable.ic_stop)
            pointsList.clear()
            mainViewModel.clearLocData()
        } else {
            activity?.stopService(Intent(activity, LocationService::class.java))
            mainViewModel.stopTimer()
            binding.buttonStartStop.setImageResource(R.drawable.ic_play)
            setSaveDialog(locTrackForSaved)
            locTrackForSaved = LocationTrack()
        }
        isServiceLocRunning = !isServiceLocRunning
    }

    private fun checkLocationServiseState() {
        mainViewModel.checkedLocationServiceState()
        mainViewModel.getStateService().observe(viewLifecycleOwner) {
            isServiceLocRunning = it
            if (isServiceLocRunning) {
                binding.buttonStartStop.setImageResource(R.drawable.ic_stop)
                mainViewModel.startTimer()
            } else {
                binding.buttonStartStop.setImageResource(R.drawable.ic_play)
            }
        }
    }

    fun setOnClicks() = with(binding) {
        val listner = onClick()
        buttonStartStop.setOnClickListener(listner)
    }

    private fun addPoint(point: GeoPoint) {
        polyLine?.addPoint(point)
    }

    private fun refreshPoints(list: List<GeoPoint>) {
        list.forEach {
            polyLine?.addPoint(it)
        }
    }

    private fun onClick(): OnClickListener {
        return OnClickListener {
            when (it.id) {
                R.id.button_start_stop -> {
                    startStopService()
                }

                R.id.button_my_position -> {
                    showMyLocation()
                }
            }
        }
    }

    private fun setSaveDialog(locTrack: LocationTrack) {
        if (locTrack.geoPointList.isNotEmpty()) {
            val saveDialogEditText = EditText(requireContext())
            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setCancelable(false)
                .setTitle(getString(R.string.save_track_dialog_title))
                .setMessage(getString(R.string.save_track_dialog_message))
                .setView(saveDialogEditText)
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    mainViewModel.saveLocationTrack(locTrack.copy(locName = saveDialogEditText.text.toString()))
                }
                .setNegativeButton(getString(R.string.no)) { _, _ ->

                }.show()
        }
    }

    private fun processingResult(locData: LocationTrack) = with(binding) {
        tvSpeed.text =
            "${tvSpeed.text.split(':')[0]}: ${locData.speed} km/h"
        binding.tvDistance.text =
            "${tvDistance.text.split(':')[0]}: ${locData.distance} m"
        tvAverageSpeed.text =
            "${tvAverageSpeed.text.split(':')[0]}: ${locData.averageSpeed} km/h"
        if (pointsList.isEmpty()) {
            pointsList.addAll(locData.geoPointList)
            refreshPoints(pointsList)
        } else {
            pointsList.add(locData.geoPointList[pointsList.size - 1])
            addPoint(pointsList[pointsList.size - 1])
        }
        locTrackForSaved = LocationTrack(
            distance = locData.distance,
            averageSpeed = locData.averageSpeed,
            geoPointList = pointsList
        )
    }
}