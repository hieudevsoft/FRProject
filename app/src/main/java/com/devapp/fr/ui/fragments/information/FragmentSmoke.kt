package com.devapp.fr.ui.fragments.information

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devapp.fr.adapters.RadioAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentSmokeBinding
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.util.DataHelper.getListSmoke
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentSmoke : BaseFragment<FragmentSmokeBinding>() {
    val TAG = "FragmentSmoke"
    private lateinit var radioAdapter: RadioAdapter
    private val args:FragmentSmokeArgs by navArgs()
    private val sharedViewModel:SharedViewModel by activityViewModels()
    override fun onSetupView() {
        radioAdapter = RadioAdapter {
                index,isChecked->
            sharedViewModel.setSharedFlowSmoke(index)
        }
        binding.rcvSmoke.apply {
            itemAnimator = null
            adapter = radioAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (args.isSingleNavigate) binding.ibBack.toVisible()
        if (args.selected!=-1) {
            val listSubmit = getListSmoke().also { it[args.selected].isChecked = true }
            radioAdapter.submitList(listSubmit)
        } else radioAdapter.submitList(getListSmoke())

        binding.ibBack.setOnClickWithAnimationListener {
            findNavController().popBackStack()
        }
        super.onViewCreated(view, savedInstanceState)
    }


}