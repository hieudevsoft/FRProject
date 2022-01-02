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

    fun saveGender(gender: Int) {
        pref.edit().putInt(Constants.KEY_GENDER,gender).apply()
    }

    fun readGender():Int {
        return pref.getInt(Constants.KEY_GENDER,0)!!
    }

    fun saveEmail(email: String) {
        pref.edit().putString(Constants.KEY_EMAIL,email).apply()
    }

    fun readEmail(): String? {
        return pref.getString(Constants.KEY_EMAIL,"")
    }

    fun savePassword(password: String) {
        pref.edit().putString(Constants.KEY_PASSWORD,password).apply()
    }

    fun readPassword(): String? {
        return pref.getString(Constants.KEY_PASSWORD,"")
    }

    fun saveIsLogin(isLogin:Boolean) {
        pref.edit().putBoolean(Constants.KEY_IS_LOGIN,isLogin).apply()
    }

    fun readIsLogin(): Boolean {
        return pref.getBoolean(Constants.KEY_IS_LOGIN,false)
    }

    fun saveIsRegister(isRegister:Boolean) {
        pref.edit().putBoolean(Constants.KEY_IS_REGISTER,isRegister).apply()
    }

    fun getIsRegister(): Boolean {
        return pref.getBoolean(Constants.KEY_IS_REGISTER,false)
    }
}