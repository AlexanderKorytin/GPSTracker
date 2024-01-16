package com.example.korytingpstracker.main_menu.data.network

import android.content.Context
import android.content.SharedPreferences
import com.example.korytingpstracker.main_menu.data.api.MapClient
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig

class OpenStreetMapClient(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) : MapClient {
    override fun configureMap() {
        Configuration.getInstance().load(context, sharedPreferences)
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
    }
}