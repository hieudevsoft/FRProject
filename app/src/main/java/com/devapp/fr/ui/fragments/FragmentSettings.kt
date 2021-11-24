package com.devapp.fr.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.devapp.fr.R
import com.devapp.fr.app.DarkTheme
import com.devapp.fr.app.LightTheme
import com.devapp.fr.app.MyAppTheme
import com.devapp.fr.databinding.FragmentSettingsBinding
import com.devapp.fr.util.DataStoreHelper
import com.devapp.fr.util.SharedPreferencesHelper
import com.devapp.fr.util.dataStore
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.Coordinate
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.dolatkia.animatedThemeManager.ThemeManager
import kotlinx.coroutines.flow.collectLatest


class FragmentSettings : ThemeFragment() {
    val TAG = "FragmentSettings"
    private var _binding:FragmentSettingsBinding?=null
    private val binding get() = _binding!!
    private lateinit var sharedPreferencesHelper:SharedPreferencesHelper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //Init SharedPreferencesHelper
        sharedPreferencesHelper= SharedPreferencesHelper(requireParentFragment().requireContext())

        //Setup Theme
        lifecycleScope.launchWhenStarted {
            parentFragment?.context?.let {
                DataStoreHelper.getInstance().getDarkMode(it.dataStore).collectLatest {
                    setupTheme(it)
                }
            }
        }

        //Init switch
        binding.switchDarkMode.isChecked = sharedPreferencesHelper.readDarkMode()

        //Handle event elements
        handleEventElement()

        super.onViewCreated(view, savedInstanceState)
    }

    override fun syncTheme(appTheme: AppTheme) {
        val theme = appTheme as MyAppTheme
        binding.tvDarkMode.setTextColor(theme.textColor(requireParentFragment().requireContext()))
    }

    private fun handleEventElement() {
        binding.switchDarkMode.setOnCheckedChangeListener { _, b ->
            (parentFragment as FragmentMainViewPager).triggerSaveDarkMode(b)
            sharedPreferencesHelper.saveDarkMode(b)
            if(!b)
            ThemeManager.instance.changeTheme(LightTheme(), Coordinate(300,300),600)
            else ThemeManager.instance.changeTheme(DarkTheme(), Coordinate(300,300),600)
        }
    }

    private fun setupTheme(isDarkMode:Boolean) {
        if(isDarkMode){
            binding.tvDarkMode.setTextColor(Color.WHITE)
        } else{
            binding.tvDarkMode.setTextColor(Color.BLACK)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView ")
        super.onDestroyView()
    }
}