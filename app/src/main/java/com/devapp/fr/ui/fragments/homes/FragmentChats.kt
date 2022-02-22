package com.devapp.fr.ui.fragments.homes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.devapp.fr.R
import com.devapp.fr.adapters.AccountOnlineAdapter
import com.devapp.fr.adapters.InformationAccountChatAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.entities.UserProfile
import com.devapp.fr.databinding.FragmentChatsBinding
import com.devapp.fr.ui.viewmodels.RealTimeViewModel
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.util.UiHelper.sendDataToViewPartnerProfile
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenResumed
import com.devapp.fr.util.storages.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@AndroidEntryPoint
class FragmentChats(private val eventListener: EventListener) : BaseFragment<FragmentChatsBinding>() {
    @Inject
    lateinit var prefs:SharedPreferencesHelper
    val TAG = "FragmentChats"
    private val sharedViewModel:SharedViewModel by activityViewModels()
    private  val realTimeViewModel: RealTimeViewModel by activityViewModels()
    private lateinit var accountAdapter:AccountOnlineAdapter
    private lateinit var informationAccountChatAdapter: InformationAccountChatAdapter
    interface EventListener {
        fun onCardChatClickListener(user:UserProfile)
    }
    private var idUser = ""
    private val realtimeViewModel:RealTimeViewModel by activityViewModels()
    override fun onSetupView() {
        idUser = prefs.readIdUserLogin()!!
        binding.rcChatOnline.apply {
            accountAdapter = AccountOnlineAdapter(this@FragmentChats,realtimeViewModel)
            accountAdapter.setOnItemClickListener { view, userProfile ->
                requireActivity().sendDataToViewPartnerProfile(view,userProfile)
            }
            adapter = accountAdapter
            itemAnimator = SlideInLeftAnimator()
        }

        binding.rcItemInformation.apply {
            informationAccountChatAdapter = InformationAccountChatAdapter(idUser,this@FragmentChats,realtimeViewModel)
            informationAccountChatAdapter.setOnItemClickListener { userProfile ->
                eventListener.onCardChatClickListener(userProfile)
            }
            layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            adapter = informationAccountChatAdapter
            itemAnimator = SlideInRightAnimator()
        }



        launchRepeatOnLifeCycleWhenResumed { 
            sharedViewModel.getSharedFlowListUserMatchByMe()
                .distinctUntilChanged()
                .collect {
                    binding.edtFilter.addTextChangedListener {text->
                        informationAccountChatAdapter.submitList(it.filter { it.name.contains(text.toString(),true) })
                    }
                    accountAdapter.submitList(it)
                    informationAccountChatAdapter.submitList(it)
                    Log.d(TAG, "onSetupView: $it")
                }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }



}