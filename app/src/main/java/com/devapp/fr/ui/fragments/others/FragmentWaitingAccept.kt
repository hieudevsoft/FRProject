package com.devapp.fr.ui.fragments.others

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.devapp.fr.R
import com.devapp.fr.adapters.WaitingAcceptAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentWaitingAcceptBinding
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.viewmodels.AuthAndProfileViewModel
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.ui.widgets.CustomDialog
import com.devapp.fr.util.Constants
import com.devapp.fr.util.UiHelper.sendDataToViewPartnerProfile
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenResumed
import com.devapp.fr.util.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.ScaleInBottomAnimator
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@AndroidEntryPoint
class FragmentWaitingAccept: BaseFragment<FragmentWaitingAcceptBinding>() {
    val TAG = "FragmentWaitingAccept"
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var waitingAcceptAdapter:WaitingAcceptAdapter
    override fun onSetupView() {
        waitingAcceptAdapter = WaitingAcceptAdapter(this)
        {view,data-> requireActivity().sendDataToViewPartnerProfile(view,data.images!![0],data)}
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