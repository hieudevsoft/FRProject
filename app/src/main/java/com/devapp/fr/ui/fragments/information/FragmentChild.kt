package com.devapp.fr.ui.fragments.information

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devapp.fr.adapters.RadioAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentChildBinding
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.util.DataHelper.getListChild
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@AndroidEntryPoint
class FragmentChild : BaseFragment<FragmentChildBinding>() {
    val TAG = "FragmentChild"
    private lateinit var radioAdapter: RadioAdapter
    private val args: FragmentChildArgs by navArgs()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var currentPosition = -1
    override fun onSetupView() {
        radioAdapter = RadioAdapter { index, _ ->
           currentPosition = index
        }
        binding.rcvChild.apply {
            itemAnimator = null
            adapter = radioAdapter
        }
        radioAdapter.submitList(getListChild())

        launchRepeatOnLifeCycleWhenStarted {
            sharedViewModel.getSharedFlowChild().distinctUntilChanged().collectLatest {
                try {
                    radioAdapter.setItemChecked(position = it)
                }catch (e:Exception){

                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (args.isSingleNavigate) binding.ibBack.toVisible()
        binding.ibBack.setOnClickWithAnimationListener {
            sharedViewModel.setSharedFlowChild(currentPosition)
            findNavController().popBackStack()
        }
        super.onViewCreated(view, savedInstanceState)
    }

}