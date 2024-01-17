package com.example.korytingpstracker.settings.ui.data.storage

import android.content.SharedPreferences
import com.example.korytingpstracker.app.IS_NEED_SHOW_DIALOG

class SharedPrefStorage(private val sharedPreferences: SharedPreferences) : SettingsStorage {
    override fun saveIsNeedDialogShow(value: Boolean) {
        sharedPreferences.edit().putBoolean(IS_NEED_SHOW_DIALOG, value).apply()
    }
}