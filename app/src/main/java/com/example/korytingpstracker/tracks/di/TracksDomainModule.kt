package com.example.korytingpstracker.tracks.di

import com.example.korytingpstracker.tracks.data.repository.LocTrackRepositoryImpl
import com.example.korytingpstracker.tracks.domain.api.LocationTrackInteractor
import com.example.korytingpstracker.tracks.domain.api.LocationTrackRepository
import com.example.korytingpstracker.tracks.domain.impl.LocTrackInteractorImpl
import org.koin.dsl.module

val tracksDomainModule = module {
    single<LocationTrackRepository> {
        LocTrackRepositoryImpl(db = get())
    }

    factory<LocationTrackInteractor> {
        LocTrackInteractorImpl(locRepository = get())
    }
}