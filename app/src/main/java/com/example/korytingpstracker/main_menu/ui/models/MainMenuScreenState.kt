package com.example.korytingpstracker.main_menu.ui.models

sealed interface MainMenuScreenState {
    data class Content(val data: LocationTrack) : MainMenuScreenState
}
