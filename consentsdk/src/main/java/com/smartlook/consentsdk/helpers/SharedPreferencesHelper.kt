package com.smartlook.consentsdk.helpers

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) : ContextWrapper(context.applicationContext) {

    companion object {
        private const val CONSENT_SDI_SHARED_PREFERENCES = "com.smartlook.consentsdk.sharedpreferences"
    }

    private val sharedPreferences: SharedPreferences
            = getSharedPreferences(CONSENT_SDI_SHARED_PREFERENCES, Context.MODE_PRIVATE)

    fun saveBoolean(key: String, state: Boolean) {
        sharedPreferences.edit().putBoolean(key, state).apply()
    }

    fun loadBoolean(key: String) = sharedPreferences.getBoolean(key, false)

}