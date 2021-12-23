package com.devapp.fr.ui.fragments.configProfile

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devapp.fr.R
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentImageBinding
import com.devapp.fr.ui.activities.ConfigProfileActivity
import com.devapp.fr.util.storages.SharedPreferencesHelper

class FragmentImage : BaseFragment<FragmentImageBinding>() {
    private lateinit var prefs:SharedPreferencesHelper
    val TAG = "FragmentImage"
    override fun onAttach(context: Context) {
        super.onAttach(context)
        prefs = (context as ConfigProfileActivity).sharedPrefs
    }
    
    override fun onSetupView() {
        Log.d(TAG, "onSetupView: ${prefs.readGender()} ${prefs.readNameConfig()} ${prefs.readEmail()} ${prefs.readDobConfig()} ${prefs.readPassword()} ${prefs.readAddress()}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}