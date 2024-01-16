package com.example.korytingpstracker.settings.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.preference.PreferenceFragmentCompat
import com.example.korytingpstracker.R

class SettingsFragment : PreferenceFragmentCompat() {

    private var _fragmentView: View? = null
    private val fragmentView get() = _fragmentView!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _fragmentView = view
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_settigs, rootKey)
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
}