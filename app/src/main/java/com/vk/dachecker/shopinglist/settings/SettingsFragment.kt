package com.vk.dachecker.shopinglist.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.vk.dachecker.shopinglist.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preference, rootKey)
    }
}