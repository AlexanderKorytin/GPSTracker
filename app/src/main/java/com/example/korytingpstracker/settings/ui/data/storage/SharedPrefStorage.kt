package com.example.korytingpstracker.settings.ui.data.storage

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import com.example.korytingpstracker.R
import com.example.korytingpstracker.app.IS_NEED_SHOW_DIALOG
import com.example.korytingpstracker.settings.ui.models.AppSettingsPrefKeys

class SharedPrefStorage(
    private val sharedPreferences: SharedPreferences,
    private val settingsPref: SharedPreferences,
    private val context: Context
) : SettingsStorage {
    override fun saveIsNeedDialogShow(value: Boolean) {
        sharedPreferences.edit().putBoolean(IS_NEED_SHOW_DIALOG, value).apply()
    }

    override fun getColorLocationTrackLine(): Int {
        val colorLineCurrent = settingsPref.getString(
            context.getString(AppSettingsPrefKeys.COLORLINE.value),
            context.resources.getStringArray(R.array.color_line_value)[0]
        )
        return Color.parseColor(colorLineCurrent)
    }

    override fun getLocationUpdateTime(): Long {
        val updateTimeCurrent = settingsPref.getString(
            context.getString(AppSettingsPrefKeys.TIMEPREFKEY.value),
            context.resources.getStringArray(R.array.location_time_update_value)[0]
        )
        return updateTimeCurrent?.toLong()
            ?: context.resources.getStringArray(R.array.location_time_update_value)[0].toLong()
    }
}