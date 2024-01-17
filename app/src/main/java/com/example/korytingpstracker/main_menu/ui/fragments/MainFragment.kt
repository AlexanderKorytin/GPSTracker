package com.example.korytingpstracker.main_menu.ui.fragments

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.korytingpstracker.R
import com.example.korytingpstracker.app.App
import com.example.korytingpstracker.databinding.FragmentMainBinding
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
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by viewModel<MainViewModel>()
    private val requester = PermissionRequester.instance()
    private lateinit var myLocationNewOverlay: MyLocationNewOverlay
    private lateinit var myGPSProvider: GpsMyLocationProvider
    private var isGpsEnabled : MutableLiveData<Boolean> = MutableLiveData(false)

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
        val locManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        isGpsEnabled.value = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        isGpsEnabled.observe(viewLifecycleOwner) {
            if (it){
                mainViewModel.getProvider()
                checkPermission()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isGpsEnabled.value == false){
            isGpsSwitchOn()
        }

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

    private fun isGpsSwitchOn() {
        val locManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var isEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isEnabled) {
            MaterialAlertDialogBuilder(requireContext())
                .setCancelable(false)
                .setTitle("Gps")
                .setMessage("switch Gps")
                .setPositiveButton("Yes") { dialog, which ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    requireActivity().startActivity(intent)
                }
                .setNegativeButton("No") { dialog, which ->
                    requireActivity().finish()
                }.show()
        }
        isGpsEnabled.postValue(isEnabled)

    }

    private fun checkPermission() {
        val arrayPermissionResult = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            arrayPermissionResult.addAll(
                arrayOf(
                    ACCESS_FINE_LOCATION,
                    ACCESS_BACKGROUND_LOCATION,
                )
            )
        } else {
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
                        getResultFineLocation(result)
                    }
                }

                ACCESS_BACKGROUND_LOCATION -> {
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                        requester.request(permission).collect { result ->
                            getResultBackGroundLocation(result)
                        }
                    }
                    if (App.needDialogShow && (Build.VERSION.SDK_INT != Build.VERSION_CODES.Q)) {
                        MaterialAlertDialogBuilder(requireContext())
                            .setCancelable(false)
                            .setTitle(requireContext().getString(R.string.dialog_back_loc_title))
                            .setMessage(requireContext().getString(R.string.dialog_back_loc_message))
                            .setNeutralButton(requireContext().getString(R.string.dialog_back_loc_neutral)) { dialog, which ->
                                lifecycleScope.launch(Dispatchers.IO) {
                                    requester.request(permission).collect { result ->
                                        App.needDialogShow = false
                                        mainViewModel.saveIsNeedShowDialog(App.needDialogShow)
                                        getResultBackGroundLocation(result)
                                    }
                                }
                            }.show()
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
            is PermissionResult.Denied -> {}
            // Запрещено навсегда, перезапрашивать нет смысла, предлагаем пройти в настройки
            is PermissionResult.Denied.DeniedPermanently -> {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.data = Uri.fromParts("package", requireActivity().packageName, null)
                requireActivity().startActivity(intent)
            }

            is PermissionResult.Denied.NeedsRationale -> {

            }
            // Запрещено навсегда, перезапрашивать нет смысла, предлагаем пройти в настройки
            is PermissionResult.Cancelled -> {
                return
            }
        }
    }

    private fun getResultBackGroundLocation(result: PermissionResult) {
        when (result) {
            // Пользователь дал разрешение, можно продолжать работу
            is PermissionResult.Granted -> {
                App.needDialogShow = false
                mainViewModel.saveIsNeedShowDialog(App.needDialogShow)
            }
            //Пользователь отказал в предоставлении разрешения
            is PermissionResult.Denied -> {
            }
            // Запрещено навсегда, перезапрашивать нет смысла, предлагаем пройти в настройки
            is PermissionResult.Denied.NeedsRationale -> {
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


}