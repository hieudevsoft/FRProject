package com.devapp.fr.ui.fragments.homes

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.devapp.fr.app.MyAppTheme
import com.devapp.fr.databinding.FragmentSettingsBinding
import com.devapp.fr.network.NotificationService
import com.devapp.fr.ui.viewmodels.RealTimeViewModel
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.util.UiHelper.findOnClickListener
import com.devapp.fr.util.UiHelper.toGone
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.animations.AnimationHelper.startAnimClick
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenCreated
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenResumed
import com.devapp.fr.util.storages.DataStoreHelper
import com.devapp.fr.util.storages.SharedPreferencesHelper
import com.devapp.fr.util.storages.dataStore
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@AndroidEntryPoint
class FragmentSettings(private val eventListener: EventListener) : ThemeFragment() {
    val TAG = "FragmentSettings"
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataStoreHelper: DataStoreHelper
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var dataStore: DataStore<Preferences>
    private val sharedViewModel: SharedViewModel by activityViewModels()
    @Inject
    lateinit var pref:SharedPreferencesHelper
    interface EventListener {
        fun onCardProfileClickListener()
        fun onCardLogoutClickListener()
        fun onCardWaitingAcceptClickListener()
        fun onCardNotificationMatchClickListener()
        fun onCardAboutsUsClickListener()
        fun onCardHelpClickListener()
    }

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
        sharedPreferencesHelper = SharedPreferencesHelper(requireParentFragment().requireContext())

        //Setup Theme
        lifecycleScope.launchWhenStarted {
            parentFragment?.context?.let {
                DataStoreHelper.getInstance().getDarkMode(it.dataStore).collectLatest {
                    setupTheme(it)
                }
            }
        }


        //Init switch
        //binding.switchDarkMode.isOn = sharedPreferencesHelper.readDarkMode()

        //Handle event elements
        handleEventElement()

        subscriberObserver()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun subscriberObserver() {
        launchRepeatOnLifeCycleWhenCreated{
            sharedViewModel.getSharedFlowListUserWaitingAccept()
                .distinctUntilChanged()
                .collect {
                    if (it.isNotEmpty()) {
                        binding.apply {
                            tvNumberWaitingAccept.toVisible()
                            tvNumberWaitingAccept.text =
                                if (it.size > 99) "99+" else it.size.toString()
                        }
                    } else binding.tvNumberWaitingAccept.toGone()
                }
        }

        launchRepeatOnLifeCycleWhenCreated {
            sharedViewModel.getSharedFlowListUserMatch()
                .distinctUntilChanged()
                .collect {
                    if (it.isNotEmpty()) {
                        binding.apply {
                            tvCountMatch.toVisible()
                            tvCountMatch.text = if (it.size > 99) "99+" else it.size.toString()
                        }
                    } else binding.tvCountMatch.toGone()
                }
        }
    }


    override fun syncTheme(appTheme: AppTheme) {
        val theme = appTheme as MyAppTheme
        binding.tvSettings.setTextColor(theme.textColor(requireContext()))
        binding.root.setBackgroundColor(theme.backgroundColor(requireContext()))
        binding.appBarLayout.setBackgroundColor(theme.backgroundColor(requireContext()))
    }

    private fun handleEventElement() {
//        binding.switchDarkMode.setOnToggledListener { _, b ->
//            triggerSaveDarkMode(b)
//            sharedPreferencesHelper.saveDarkMode(b)
//            if(!b)
//            ThemeManager.instance.changeTheme(LightTheme(), Coordinate(300,300),600)
//            else ThemeManager.instance.changeTheme(DarkTheme(), Coordinate(300,300),600)
//        }
        findOnClickListener(
            binding.lyNotifications,
            binding.cardLogout,
            binding.cardShowProfile,
            binding.lyWaitingAccept,
            binding.lyAboutUs,
            binding.lyHelp,
        ) {
            when (this) {
                binding.cardLogout -> {
                    binding.cardLogout.startAnimClick()
                    sharedPreferencesHelper.saveIsLogin(false)
                    sharedPreferencesHelper.saveIdUserLogin("")
                    Toast.makeText(requireActivity(), "Đăng xuất thành công ~", Toast.LENGTH_SHORT)
                        .show()
                    eventListener.onCardLogoutClickListener()
                }
                binding.cardShowProfile -> {
                    binding.cardShowProfile.startAnimClick()
                    eventListener.onCardProfileClickListener()
                }
                binding.lyWaitingAccept -> {
                    binding.lyWaitingAccept.startAnimClick()
                    eventListener.onCardWaitingAcceptClickListener()
                }
                binding.lyNotifications -> {
                    binding.lyNotifications.startAnimClick()
                    eventListener.onCardNotificationMatchClickListener()
                }
                binding.lyAboutUs->{
                    binding.lyAboutUs.startAnimClick()
                    eventListener.onCardAboutsUsClickListener()
                }
                binding.lyHelp->{
                    binding.lyHelp.startAnimClick()
                    eventListener.onCardHelpClickListener()
                }
            }
        }
    }

//    fun triggerSaveDarkMode(value:Boolean){
//        viewLifecycleOwner.lifecycleScope.launch {
//            dataStoreHelper.saveDarkMode(dataStore,value)
//        }
//    }

    private fun setupTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            // DARK MODE
        } else {
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