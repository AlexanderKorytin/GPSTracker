package com.example.korytingpstracker.main_menu.di

import com.example.korytingpstracker.main_menu.ui.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainVMModule = module {
    viewModel<MainViewModel> { MainViewModel(interactor = get()) }
}