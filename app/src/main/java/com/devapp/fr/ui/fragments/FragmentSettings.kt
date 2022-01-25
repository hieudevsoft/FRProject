package com.devapp.fr.ui.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.devapp.fr.app.DarkTheme
import com.devapp.fr.app.LightTheme
import com.devapp.fr.app.MyAppTheme
import com.devapp.fr.databinding.FragmentSettingsBinding
import com.devapp.fr.util.UiHelper.findOnClickListener
import com.devapp.fr.util.storages.DataStoreHelper
import com.devapp.fr.util.storages.SharedPreferencesHelper
import com.devapp.fr.util.storages.dataStore
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.Coordinate
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.dolatkia.animatedThemeManager.ThemeManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FragmentSettings : ThemeFragment() {
    val TAG = "FragmentSettings"
    private var _binding:FragmentSettingsBinding?=null
    private val binding get() = _binding!!
    private lateinit var dataStoreHelper: DataStoreHelper
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var dataStore: DataStore<Preferences>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onAttach(context: Context) {
        dataStore = context.dataStore
        dataStoreHelper = DataStoreHelper.getInstance()
        super.onAttach(context)
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
        binding.switchDarkMode.isOn = sharedPreferencesHelper.readDarkMode()

        //Handle event elements
        handleEventElement()

        super.onViewCreated(view, savedInstanceState)
    }

    override fun syncTheme(appTheme: AppTheme) {
        val theme = appTheme as MyAppTheme
        binding.tvSettings.setTextColor(theme.textColor(requireContext()))
        binding.root.setBackgroundColor(theme.backgroundColor(requireContext()))
        binding.appBarLayout.setBackgroundColor(theme.backgroundColor(requireContext()))
    }

    private fun handleEventElement() {
        binding.switchDarkMode.setOnToggledListener { _, b ->
            triggerSaveDarkMode(b)
            sharedPreferencesHelper.saveDarkMode(b)
            if(!b)
            ThemeManager.instance.changeTheme(LightTheme(), Coordinate(300,300),600)
            else ThemeManager.instance.changeTheme(DarkTheme(), Coordinate(300,300),600)
        }
        findOnClickListener(binding.cardLogout){
            when(this){
                binding.cardLogout->{
                    sharedPreferencesHelper.saveIsLogin(false)
                    findNavController().navigate(FragmentSettingsDirections.actionFragmentSettingsToFragmentLogin())
                }
            }
        }
    }

    fun triggerSaveDarkMode(value:Boolean){
        viewLifecycleOwner.lifecycleScope.launch {
            dataStoreHelper.saveDarkMode(dataStore,value)
        }
    }

    private fun setupTheme(isDarkMode:Boolean) {
        if(isDarkMode){
            // DARK MODE
        } else{
            //LIGHT MODE
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