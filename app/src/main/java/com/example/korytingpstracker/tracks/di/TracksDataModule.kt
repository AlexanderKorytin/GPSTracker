package com.example.korytingpstracker.tracks.di

import androidx.room.Room
import com.example.korytingpstracker.core.ui.db.AppDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val tracksDataModule= module {
    single {
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "app_db.db").build()
    }
}