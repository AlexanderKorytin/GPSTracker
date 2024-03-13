package com.example.korytingpstracker.core.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.korytingpstracker.R
import com.example.korytingpstracker.databinding.ActivityMainBinding
import com.example.korytingpstracker.main_menu.data.service.LocationService

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.currentTrackFragment -> {
                    binding.bottomNav.isVisible = false
                }

                else -> {
                    binding.bottomNav.isVisible = true
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        val isGranted = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        if (isGranted) {
            this.stopService(Intent(this, LocationService::class.java))
        }
    }
}