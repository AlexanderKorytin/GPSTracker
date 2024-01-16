package com.example.korytingpstracker.settings.ui.fragments

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
        timePref = findPreference(AppSettingsPrefKeys.TIMEPREFKEY.value)!!
        val listener = onPreferenceChangeListner()
        timePref.onPreferenceChangeListener = listener
        initPref()
    }

    private fun onPreferenceChangeListner(): OnPreferenceChangeListener {
        return OnPreferenceChangeListener { pref, value ->
            when (pref.key) {
                AppSettingsPrefKeys.TIMEPREFKEY.value -> {
                    pref.title = setTitleUpdateTime(pref, value.toString())
                }
            }
            true
        }
    }

    private fun initPref() {
        val updateTimeCurrent = timePref.preferenceManager.sharedPreferences?.getString(
            AppSettingsPrefKeys.TIMEPREFKEY.value,
            "3000"
        )
        timePref.title = setTitleUpdateTime(timePref, updateTimeCurrent)
    }

    private fun setTitleUpdateTime(pref: Preference, currentValue: String?): String {
        val nameArray = resources.getStringArray(R.array.location_time_update_name)
        val valueArray = resources.getStringArray(R.array.location_time_update_value)
        return "${
            pref.title?.split(separator)?.get(0) ?: getString(R.string.update_time)
        }: ${nameArray[valueArray.indexOf(currentValue)]}"
    }
}