package com.example.korytingpstracker.main_menu.di

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.korytingpstracker.main_menu.data.api.MapClient
import com.example.korytingpstracker.main_menu.data.network.OpenStreetMapClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val OSM = "osm"

val mainDataModule = module {

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            OSM,
            AppCompatActivity.MODE_PRIVATE
        )
    }

    single<MapClient> { OpenStreetMapClient(androidContext(), sharedPreferences = get()) }
}