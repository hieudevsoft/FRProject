package com.devapp.fr.ui.fragments.others

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.devapp.fr.R
import com.devapp.fr.adapters.NotificationsMatchAdapter
import com.devapp.fr.adapters.WaitingAcceptAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentNotificationMatchBinding
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.viewmodels.AuthAndProfileViewModel
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.ui.widgets.CustomDialog
import com.devapp.fr.util.Constants
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenResumed
import com.devapp.fr.util.extensions.showToast
import com.devapp.fr.util.storages.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@AndroidEntryPoint
class FragmentNotificationMatch:BaseFragment<FragmentNotificationMatchBinding>(){
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val authAndProfileViewModel: AuthAndProfileViewModel by activityViewModels()
    private lateinit var adapter:NotificationsMatchAdapter
    private lateinit var loadingDialog:CustomDialog
    private var partnerName:String?=""
    var currentPosition = -1
    @Inject
    lateinit var prefs:SharedPreferencesHelper
    override fun onSetupView() {
        loadingDialog = CustomDialog(R.layout.dialog_loading)
        adapter = NotificationsMatchAdapter(this,
            {view,position->
                currentPosition = position
                partnerName = adapter.getItemAtPostion(position).name
                authAndProfileViewModel.acceptOrCancel(adapter.getItemAtPostion(position).id,prefs.readIdUserLogin().toString(),false)
            },
            { view,position->
                currentPosition = position
                partnerName = adapter.getItemAtPostion(position).name
                authAndProfileViewModel.acceptOrCancel(adapter.getItemAtPostion(position).id,prefs.readIdUserLogin().toString(),true)
            },
            {
                view,data->//show profile
            }
        )
        binding.rcNotifications.apply {
            itemAnimator = null
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = this@FragmentNotificationMatch.adapter
        }

        subscriberObserver()
        binding.apply {
            btnBack.setOnClickWithAnimationListener {
                findNavController().popBackStack() }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun subscriberObserver() {
        launchRepeatOnLifeCycleWhenResumed {
            sharedViewModel.getSharedFlowListUserMatch()
                .distinctUntilChanged()
                .collect {
                    adapter.submitList(it)
                }
        }

        launchRepeatOnLifeCycleWhenResumed {
            authAndProfileViewModel.stateAcceptOrCancel.collect {
                when (it) {
                    is ResourceRemote.Loading -> {
                        loadingDialog.show(childFragmentManager,loadingDialog.tag)
                    }

                    is ResourceRemote.Success -> {
                        val listNew = adapter.getListCurrent().toMutableList()
                        listNew.removeAt(currentPosition)
                        adapter.submitList(listNew)
                        sharedViewModel.setSharedFlowListUserMatch(listNew)
                        showToast("Đợi chút chúng tôi sẽ gửi phản hồi cho $partnerName")
                        loadingDialog.dismiss()
                    }

                    is ResourceRemote.Error -> {
                        loadingDialog.dismiss()
                        showToast("Kiểm tra kết nối mạng!!!")
                    }
                    else -> {

                    }
                }
            }
        }
    }

    override fun onDestroy() {
        authAndProfileViewModel.resetSateAcceptOrCancel()
        super.onDestroy()
    }
}