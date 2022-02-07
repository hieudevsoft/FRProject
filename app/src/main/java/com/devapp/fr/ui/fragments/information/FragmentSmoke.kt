package com.devapp.fr.ui.fragments.information

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devapp.fr.R
import com.devapp.fr.adapters.RadioAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.models.items.RadioItem
import com.devapp.fr.databinding.FragmentSmokeBinding
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.util.DataHelper.getListSmoke
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenResumed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@AndroidEntryPoint
class FragmentSmoke : BaseFragment<FragmentSmokeBinding>() {
    val TAG = "FragmentSmoke"
    private lateinit var radioAdapter: RadioAdapter
    private val args:FragmentSmokeArgs by navArgs()
    private val sharedViewModel:SharedViewModel by activityViewModels()
    private var currentPosition = -1
    override fun onSetupView() {
        radioAdapter = RadioAdapter {
                index,isChecked->
            currentPosition = index
        }
        binding.rcvSmoke.apply {
            itemAnimator = null
            adapter = radioAdapter
        }
        radioAdapter.submitList(getListSmoke())
        launchRepeatOnLifeCycleWhenResumed {
            sharedViewModel.getSharedFlowSmoke()
                .distinctUntilChanged()
                .collectLatest {
                    try{
                        radioAdapter.setItemChecked(it)
                    }catch (e:Exception){

                    }
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (args.isSingleNavigate) binding.ibBack.toVisible()
        binding.ibBack.setOnClickWithAnimationListener {
            sharedViewModel.setSharedFlowSmoke(currentPosition)
            findNavController().popBackStack()
        }
        super.onViewCreated(view, savedInstanceState)
    }


}