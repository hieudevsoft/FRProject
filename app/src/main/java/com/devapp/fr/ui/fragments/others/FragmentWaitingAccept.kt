package com.devapp.fr.ui.fragments.others

import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.devapp.fr.adapters.WaitingAcceptAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentWaitingAcceptBinding
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.util.UiHelper.sendDataToViewPartnerProfile
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenResumed
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged

@AndroidEntryPoint
class FragmentWaitingAccept: BaseFragment<FragmentWaitingAcceptBinding>() {
    val TAG = "FragmentWaitingAccept"
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var waitingAcceptAdapter:WaitingAcceptAdapter
    override fun onSetupView() {
        waitingAcceptAdapter = WaitingAcceptAdapter(this)
        {view,data-> requireActivity().sendDataToViewPartnerProfile(view, data)}
        binding.rcWaitingAccept.apply {
            itemAnimator = SlideInLeftAnimator(DecelerateInterpolator())
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = waitingAcceptAdapter
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
            sharedViewModel.getSharedFlowListUserWaitingAccept()
                .distinctUntilChanged()
                .collect {
                    waitingAcceptAdapter.submitList(it)
                }
        }
    }

}