package com.example.korytingpstracker.main_menu.di

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.korytingpstracker.R
import com.example.korytingpstracker.app.APP_SETTINGS_PREF_KEY
import com.example.korytingpstracker.core.ui.MainActivity
import com.example.korytingpstracker.main_menu.data.api.MapClient
import com.example.korytingpstracker.main_menu.data.network.OpenStreetMapClient
import com.example.korytingpstracker.main_menu.data.service.LocationService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mainDataModule = module {

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            APP_SETTINGS_PREF_KEY,
            AppCompatActivity.MODE_PRIVATE
        )
    }
    single<MapClient> { OpenStreetMapClient(androidContext(), sharedPreferences = get()) }
}