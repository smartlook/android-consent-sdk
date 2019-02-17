package com.smartlook.consentsdk.helpers

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SharedPreferencesHelper(context: Context) : ContextWrapper(context.applicationContext) {

    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

    fun saveBoolean(key: String, state: Boolean) {
        sharedPreferences.edit().putBoolean(key, state).apply()
    }

    fun loadBoolean(key: String) = sharedPreferences.getBoolean(key, false)

}