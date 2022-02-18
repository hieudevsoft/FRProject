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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devapp.fr.R
import com.devapp.fr.adapters.MainViewPagerAdapter
import com.devapp.fr.app.MyAppTheme
import com.devapp.fr.app.toBinding
import com.devapp.fr.databinding.FragmentMainViewPagerBinding
import com.devapp.fr.ui.activities.MainActivity
import com.devapp.fr.ui.fragments.homes.FragmentChats
import com.devapp.fr.ui.fragments.homes.FragmentLoves
import com.devapp.fr.ui.fragments.homes.FragmentSettings
import com.devapp.fr.ui.viewmodels.AuthAndProfileViewModel
import com.devapp.fr.util.UiHelper
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.animations.PageTransformHelper
import com.devapp.fr.util.storages.DataStoreHelper
import com.devapp.fr.util.storages.SharedPreferencesHelper
import com.devapp.fr.util.storages.dataStore
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import nl.joery.animatedbottombar.AnimatedBottomBar
import javax.inject.Inject

@AndroidEntryPoint
class FragmentMainViewPager : ThemeFragment(), FragmentSettings.EventListener {
    val TAG = "FragmentMainViewPager"
    private var _binding: FragmentMainViewPagerBinding? = null
    val binding get() = _binding!!
    private lateinit var bottomBar: AnimatedBottomBar
    private lateinit var mainViewPager: MainViewPagerAdapter
    private lateinit var dataStoreHelper: DataStoreHelper
    private lateinit var dataStore: DataStore<androidx.datastore.preferences.core.Preferences>
    private lateinit var fragmentSettings: FragmentSettings
    private val args: FragmentMainViewPagerArgs by navArgs()

    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private val authAndProfileViewModel:AuthAndProfileViewModel by activityViewModels()
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
        Log.d(TAG, "onViewCreated")
        //Get Bottom Bar from MainActivity
        if (requireActivity() is MainActivity) {
            bottomBar = (requireActivity() as MainActivity).bottomBar
        }

        //Initialize MainViewPager
        initializeMainViewPager()

        //Setup ViewPager2 with bottom bar
        bottomBar.setupWithViewPager2(binding.mainViewPager)
        bottomBar.toVisible()

        //Retrieve data
        subscribersObserve()

        super.onViewCreated(view, savedInstanceState)
    }

    override fun syncTheme(appTheme: AppTheme) {
        val theme = appTheme as MyAppTheme
        binding.mainViewPager.apply {
            setBackgroundColor(theme.backgroundColor(requireContext()))
        }
    }

    private fun handleOnBackPress(isDarkMode: Boolean) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                UiHelper.triggerBottomAlertDialog(
                    requireActivity(),
                    "Exit",
                    "Do you want exit app ?",
                    "OK",
                    "NO",
                    isDarkMode
                ) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    requireActivity().finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun initializeMainViewPager() {
        mainViewPager = MainViewPagerAdapter(makeListFragment(), childFragmentManager, lifecycle)
        binding.mainViewPager.apply {
            adapter = mainViewPager
            setPageTransformer(PageTransformHelper.TabletPageTransformer())
            offscreenPageLimit = 3
        }
    }

    private fun getIdUser(): String {
        var value = ""
        args.let {
            if (it.id.isNotEmpty()) {
                value = it.id
            } else {
                if (sharedPreferencesHelper.readIdUserLogin()!!.isNotEmpty()) {
                    value = sharedPreferencesHelper.readIdUserLogin()!!
                } else {
                    Log.d(TAG, "handleLoginEvent: error because not login")
                }
            }
        }
        return value
    }

    private fun subscribersObserve() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            dataStoreHelper.getDarkMode(dataStore).collectLatest {
                binding.mainViewPager.background = if (it) ContextCompat.getDrawable(
                    requireContext(),
                    R.color.background_dark_mode
                ) else
                    ContextCompat.getDrawable(requireContext(), R.color.background_light_mode)
                handleOnBackPress(it)

            }
        }

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun makeListFragment(): List<Fragment> {
        fragmentSettings = FragmentSettings(this)
        return listOf(
            fragmentSettings,
            FragmentChats(),
            FragmentLoves()
        )
    }

    override fun onCardProfileClickListener() {
        if (getIdUser().isNotEmpty()) findNavController().navigate(
            FragmentMainViewPagerDirections.actionFragmentMainViewPagerToFragmentProfile(
                getIdUser()
            )
        )
    }

    override fun onCardLogoutClickListener() {
        requireActivity().finish()
    }

    override fun onCardWaitingAcceptClickListener() {
        findNavController().navigate(
            FragmentMainViewPagerDirections.actionFragmentMainViewPagerToFragmentWaitingAccept(
                getIdUser()
            )
        )
    }

    override fun onCardNotificationMatchClickListener() {
        findNavController().navigate(
            FragmentMainViewPagerDirections.actionFragmentMainViewPagerToFragmentNotificationMatch()
        )
    }
}