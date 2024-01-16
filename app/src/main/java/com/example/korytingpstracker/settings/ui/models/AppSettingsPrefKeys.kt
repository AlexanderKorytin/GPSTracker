package com.example.korytingpstracker.settings.ui.models

import androidx.annotation.StringRes
import com.example.korytingpstracker.R

enum class AppSettingsPrefKeys(@StringRes val value: Int) {
    TIMEPREFKEY(R.string.update_time_key),
    COLORLINE(R.string.color_key),
}