package com.devapp.fr.util.storages

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.devapp.fr.util.Constants

class SharedPreferencesHelper(context: Context) {
    private var pref: SharedPreferences =
        context.getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE)

    fun saveDarkMode(isDarkMode: Boolean) {
        pref.edit().putBoolean(Constants.KEY_DARK_MODE_DEC,isDarkMode).apply()
    }

    fun readDarkMode():Boolean {
        return pref.getBoolean(Constants.KEY_DARK_MODE_DEC,false)
    }

    fun saveSkipSlide(isSkip: Boolean) {
        pref.edit().putBoolean(Constants.KEY_SKIP_SLIDE,isSkip).apply()
    }

    fun readSkipSlide():Boolean {
        return pref.getBoolean(Constants.KEY_SKIP_SLIDE,false)
    }

    fun saveNameConfig(name: String) {
        pref.edit().putString(Constants.KEY_NAME_CONFIG,name).apply()
    }

    fun readNameConfig():String {
        return pref.getString(Constants.KEY_NAME_CONFIG,"")!!
    }

    fun saveDobConfig(dob: String) {
        pref.edit().putString(Constants.KEY_DOB,dob).apply()
    }

    fun readDobConfig():String {
        return pref.getString(Constants.KEY_DOB,"")!!
    }

    fun saveAddress(address: String) {
        pref.edit().putString(Constants.KEY_ADDRESS,address).apply()
    }

    fun readAddress():String {
        return pref.getString(Constants.KEY_ADDRESS,"")!!
    }
}