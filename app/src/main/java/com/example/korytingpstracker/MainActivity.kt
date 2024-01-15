package com.example.korytingpstracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.korytingpstracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onBottomNavClick() {
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.id_main_menu -> {

                }

                R.id.id_tracks -> {

                }

                R.id.id_settings -> {

                }
            }
            true
        }
    }
}