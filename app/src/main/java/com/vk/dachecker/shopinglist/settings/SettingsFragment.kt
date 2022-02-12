package com.vk.dachecker.shopinglist.settings

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.vk.dachecker.shopinglist.R
import com.vk.dachecker.shopinglist.billing.BillingManager

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var removeAdsPref : Preference
    private lateinit var bManager: BillingManager

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preference, rootKey)
        init()
    }

    private fun init(){
        bManager = BillingManager(activity as AppCompatActivity)
        removeAdsPref = findPreference("remove_ads_key")!!
        removeAdsPref.setOnPreferenceClickListener {
            bManager.startConnection()
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bManager.closeConnection()
    }
}