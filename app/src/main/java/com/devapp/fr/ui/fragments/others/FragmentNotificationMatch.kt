package com.devapp.fr.ui.fragments.others

import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.devapp.fr.R
import com.devapp.fr.adapters.NotificationsMatchAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.models.items.Notification
import com.devapp.fr.databinding.FragmentNotificationMatchBinding
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.viewmodels.AuthAndProfileViewModel
import com.devapp.fr.ui.viewmodels.RealTimeViewModel
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.ui.widgets.LoadingDialog
import com.devapp.fr.util.UiHelper.sendDataToViewPartnerProfile
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenResumed
import com.devapp.fr.util.extensions.showToast
import com.devapp.fr.util.storages.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@AndroidEntryPoint
class FragmentNotificationMatch:BaseFragment<FragmentNotificationMatchBinding>(){
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val authAndProfileViewModel: AuthAndProfileViewModel by activityViewModels()
    private lateinit var adapter:NotificationsMatchAdapter
    private var partnerName:String?=""
    var currentPosition = -1
    @Inject
    lateinit var prefs:SharedPreferencesHelper
    private val realTimeViewModel: RealTimeViewModel by activityViewModels()
    override fun onSetupView() {
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
                view,data->requireActivity().sendDataToViewPartnerProfile(view,data)
            }
        )
        binding.rcNotifications.apply {
            itemAnimator = SlideInLeftAnimator(DecelerateInterpolator())
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
                        loadingDialog.show()
                    }

                    is ResourceRemote.Success -> {
                        loadingDialog.dismiss()
                        val listNew = adapter.getListCurrent().toMutableList()
                        sharedViewModel.getSharedFlowBasicInformation().distinctUntilChanged()
                            .collectLatest {
                                realTimeViewModel.sendNotificationWhenMeReply(Notification(prefs.readIdUserLogin()!!,it[0] as String),
                                    adapter.getItemAtPostion(currentPosition).id
                                )
                            }
                        listNew.removeAt(currentPosition)
                        adapter.submitList(listNew)
                        sharedViewModel.setSharedFlowListUserMatch(listNew)
                        showToast("Đợi chút chúng tôi sẽ gửi phản hồi cho $partnerName")
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

    override fun onDestroyView() {
        authAndProfileViewModel.resetSateAcceptOrCancel()
        super.onDestroyView()
    }

}