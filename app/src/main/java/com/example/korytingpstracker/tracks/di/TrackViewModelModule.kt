package com.example.korytingpstracker.tracks.di

import com.example.korytingpstracker.tracks.ui.viewmodel.LocationTrackViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val trackViewModelModule = module {
    viewModel<LocationTrackViewModel> {
        LocationTrackViewModel(dataBaseInteractor = get(), settingsInteractor = get(), mapInteractor = get())
    }
}