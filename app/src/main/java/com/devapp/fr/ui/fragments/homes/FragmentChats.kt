package com.devapp.fr.ui.fragments.homes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.devapp.fr.R
import com.devapp.fr.adapters.AccountOnlineAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentChatsBinding
import com.devapp.fr.ui.viewmodels.RealTimeViewModel
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.util.UiHelper.sendDataToViewPartnerProfile
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenResumed
import com.devapp.fr.util.storages.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@AndroidEntryPoint
class FragmentChats : BaseFragment<FragmentChatsBinding>() {
    @Inject
    lateinit var prefs:SharedPreferencesHelper
    val TAG = "FragmentChats"
    private val sharedViewModel:SharedViewModel by activityViewModels()
    private lateinit var accountAdapter:AccountOnlineAdapter
    private val realtimeViewModel:RealTimeViewModel by activityViewModels()
    override fun onSetupView() {
        binding.rcChatOnline.apply {
            accountAdapter = AccountOnlineAdapter(this@FragmentChats,viewLifecycleOwner.lifecycleScope,realtimeViewModel)
            accountAdapter.setOnItemClickListener { view, userProfile ->
                requireActivity().sendDataToViewPartnerProfile(view,userProfile)
            }
            adapter = accountAdapter
            itemAnimator = SlideInLeftAnimator()
        }
        launchRepeatOnLifeCycleWhenResumed { 
            sharedViewModel.getSharedFlowListUserMatchByMe()
                .distinctUntilChanged()
                .collect {
                    accountAdapter.submitList(it)
                    Log.d(TAG, "onSetupView: $it")
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }



}