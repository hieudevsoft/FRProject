package com.devapp.fr.ui.fragments.information

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devapp.fr.adapters.RadioAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentPetBinding
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.util.DataHelper.getListPet
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentPet : BaseFragment<FragmentPetBinding>() {
    val TAG = "FragmentPet"
    private lateinit var radioAdapter: RadioAdapter
    private val args:FragmentPetArgs by navArgs()
    private val sharedViewModel:SharedViewModel by activityViewModels()
    override fun onSetupView() {
        radioAdapter = RadioAdapter {
                index,isChecked->
            sharedViewModel.setSharedFlowPet(index)
        }
        binding.rcvPet.apply {
            itemAnimator = null
            adapter = radioAdapter
        }
        radioAdapter.submitList(getListPet())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (args.isSingleNavigate) binding.ibBack.toVisible()
        if (args.selected!=-1) {
            val listSubmit = getListPet().also { it[args.selected].isChecked = true }
            radioAdapter.submitList(listSubmit)
        } else radioAdapter.submitList(getListPet())

        binding.ibBack.setOnClickWithAnimationListener {
            findNavController().popBackStack()
        }
        super.onViewCreated(view, savedInstanceState)
    }

}