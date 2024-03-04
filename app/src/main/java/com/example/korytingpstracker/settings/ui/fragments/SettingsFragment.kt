package com.example.korytingpstracker.settings.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.preference.Preference
import androidx.preference.Preference.OnPreferenceChangeListener
import androidx.preference.PreferenceFragmentCompat
import com.example.korytingpstracker.R
import com.example.korytingpstracker.settings.ui.models.AppSettingsPrefKeys

class SettingsFragment : PreferenceFragmentCompat() {
    private val separator = ':'
    private var _fragmentView: View? = null
    private val fragmentView get() = _fragmentView!!
    private lateinit var timePref: Preference
    private lateinit var colorPref: Preference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _fragmentView = view
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_settigs, rootKey)
        getSettingsPreference()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        playExitAnim(fragmentView)
        _fragmentView = null
    }

    private fun playExitAnim(view: View) {
        val anim = AnimationUtils.loadAnimation(
            requireContext(),
            com.google.android.material.R.anim.abc_fade_in
        )
        view.startAnimation(anim)
    }

    private fun playEnterAnim(view: View) {
        val anim = AnimationUtils.loadAnimation(
            requireContext(),
            com.google.android.material.R.anim.abc_fade_out
        )
        view.startAnimation(anim)
    }

    private fun getSettingsPreference() {
        timePref = findPreference(getString(AppSettingsPrefKeys.TIMEPREFKEY.value))!!
        colorPref = findPreference(getString(AppSettingsPrefKeys.COLORLINE.value))!!
        val listener = onPreferenceChangeListner()
        timePref.onPreferenceChangeListener = listener
        colorPref.onPreferenceChangeListener = listener
        initPref()
    }

    private fun onPreferenceChangeListner(): OnPreferenceChangeListener {
        return OnPreferenceChangeListener { pref, value ->
            when (pref.key) {
                getString(AppSettingsPrefKeys.TIMEPREFKEY.value) -> {
                    pref.title = setTitleUpdateTime(value.toString())
                }

                getString(AppSettingsPrefKeys.COLORLINE.value) -> {
                    colorPref.icon?.setTint(setColorIconTrackLine(value.toString()))
                }
            }
            true
        }
    }

    private fun initPref() {
        val pref = timePref.preferenceManager.sharedPreferences

        val updateTimeCurrent = pref?.getString(
            getString(AppSettingsPrefKeys.TIMEPREFKEY.value),
            resources.getStringArray(R.array.location_time_update_value)[0]
        )
        timePref.title = setTitleUpdateTime(updateTimeCurrent)

        val colorLineCurrent = pref?.getString(
            getString(AppSettingsPrefKeys.COLORLINE.value),
            resources.getStringArray(R.array.color_line_value)[0]
        )
        colorPref.icon?.setTint(setColorIconTrackLine(colorLineCurrent))
    }

    private fun setTitleUpdateTime(currentValue: String?): String {
        val nameArray = resources.getStringArray(R.array.location_time_update_name)
        val valueArray = resources.getStringArray(R.array.location_time_update_value)
        return "${
            timePref.title?.split(separator)?.get(0) ?: getString(R.string.update_time)
        }: ${nameArray[valueArray.indexOf(currentValue)]}"
    }

    private fun setColorIconTrackLine(currentValue: String?): Int {
        return Color.parseColor(currentValue)
    }
}