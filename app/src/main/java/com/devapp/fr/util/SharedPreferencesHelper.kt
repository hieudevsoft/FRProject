package com.devapp.fr.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {
    private var pref: SharedPreferences =
        context.getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE)

    fun saveDarkMode(isDarkMode: Boolean) {
        pref.edit().putBoolean(Constants.KEY_DARK_MODE_DEC,isDarkMode).apply()
    }

    fun readDarkMode():Boolean {
        return pref.getBoolean(Constants.KEY_DARK_MODE_DEC,false)
    }
}