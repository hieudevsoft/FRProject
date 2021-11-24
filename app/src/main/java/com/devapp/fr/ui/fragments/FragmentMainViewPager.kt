package com.devapp.fr.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.devapp.fr.R
import com.devapp.fr.adapters.MainViewPagerAdapter
import com.devapp.fr.app.MyAppTheme
import com.devapp.fr.databinding.FragmentMainViewPagerBinding
import com.devapp.fr.ui.MainActivity
import com.devapp.fr.util.DataStoreHelper
import com.devapp.fr.util.PageTransformHelper
import com.devapp.fr.util.UiHelper
import com.devapp.fr.util.dataStore
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import nl.joery.animatedbottombar.AnimatedBottomBar

class FragmentMainViewPager : ThemeFragment() {
    val TAG = "FragmentMainViewPager"
    private var _binding: FragmentMainViewPagerBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomBar:AnimatedBottomBar
    private lateinit var mainViewPager:MainViewPagerAdapter
    private lateinit var dataStoreHelper: DataStoreHelper
    private lateinit var dataStore:DataStore<androidx.datastore.preferences.core.Preferences>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainViewPagerBinding.inflate(inflater)
        return binding.root
    }

    override fun onAttach(context: Context) {
        dataStore = context.dataStore
        dataStoreHelper = DataStoreHelper.getInstance()
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //Get Bottom Bar from MainActivity
        if(requireActivity() is MainActivity){
            bottomBar = (requireActivity() as MainActivity).bottomBar
        }

        //Initialize MainViewPager
        initializeMainViewPager()

        //Setup ViewPager2 with bottom bar
        bottomBar.setupWithViewPager2(binding.mainViewPager)

        //Retrieve data
        subscribersObserve()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun syncTheme(appTheme: AppTheme) {
        val theme = appTheme as MyAppTheme
        binding.mainViewPager.setBackgroundColor(theme.backgroundColor(requireContext()))
    }

    private fun handleOnBackPress(isDarkMode:Boolean) {
        val callback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                UiHelper.triggerBottomAlertDialog(
                    requireActivity(),
                    "Exit",
                    "Do you want exit app ?",
                    "OK",
                    "NO",
                    isDarkMode
                ){
                        dialogInterface, _ ->
                    dialogInterface.dismiss()
                    requireActivity().finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun initializeMainViewPager() {
        mainViewPager = MainViewPagerAdapter(makeListFragment(),childFragmentManager,lifecycle)
        binding.mainViewPager.apply {
            adapter = mainViewPager
            setPageTransformer(PageTransformHelper.TabletPageTransformer())
        }
    }

    fun triggerSaveDarkMode(value:Boolean){
        viewLifecycleOwner.lifecycleScope.launch {
            dataStoreHelper.saveDarkMode(dataStore,value)
        }
    }

    private fun subscribersObserve(){
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            dataStoreHelper.getDarkMode(dataStore).collectLatest {
                binding.mainViewPager.background = if(it) ContextCompat.getDrawable(requireContext(), R.color.background_dark_mode) else
                    ContextCompat.getDrawable(requireContext(), R.color.background_light_mode)
                handleOnBackPress(it)

            }
        }

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun makeListFragment ():List<Fragment>{
        return listOf(
            FragmentSettings(),
            FragmentChats(),
            FragmentLoves()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}