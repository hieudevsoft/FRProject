package com.devapp.fr.ui.fragments.information

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devapp.fr.adapters.RadioAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.models.items.RadioItem
import com.devapp.fr.databinding.FragmentMaritalStatusBinding
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.util.DataHelper
import com.devapp.fr.util.DataHelper.getListMaritalStatus
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@AndroidEntryPoint
class FragmentMaritalStatus : BaseFragment<FragmentMaritalStatusBinding>() {
    val TAG = "FragmentMaritalStatus"
    private lateinit var radioAdapter: RadioAdapter
    private val args:FragmentMaritalStatusArgs by navArgs()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var currentPosition = -1
    override fun onSetupView() {

        radioAdapter = RadioAdapter {
                index, _ ->
            currentPosition = index
        }
        binding.rcvSt.apply {
            itemAnimator = null
            adapter = radioAdapter
        }
        radioAdapter.submitList(getListMaritalStatus())

        launchRepeatOnLifeCycleWhenStarted {
            sharedViewModel.getSharedFlowMaritalStatus()
                .distinctUntilChanged()
                .collectLatest {
                    try {
                        radioAdapter.setItemChecked(it)
                    }catch (e:Exception){

                    }
                }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(args.isSingleNavigate){
            binding.ibBack.toVisible()
        }

        binding.ibBack.setOnClickWithAnimationListener {
            sharedViewModel.setSharedFlowMaritalStatus(currentPosition)
           findNavController().popBackStack()
        }
        super.onViewCreated(view, savedInstanceState)
    }

}