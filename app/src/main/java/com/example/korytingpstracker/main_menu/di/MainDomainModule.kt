package com.example.korytingpstracker.main_menu.di

import com.example.korytingpstracker.main_menu.data.network.MainRepositoryImpl
import com.example.korytingpstracker.main_menu.domain.api.MainInteractor
import com.example.korytingpstracker.main_menu.domain.api.MainRepository
import com.example.korytingpstracker.main_menu.domain.impl.MainInteractorImpl
import org.koin.dsl.module

val mainDomainModule = module {
    single<MainRepository> { MainRepositoryImpl(mapClient = get()) }
    factory<MainInteractor> { MainInteractorImpl(repository = get()) }
}