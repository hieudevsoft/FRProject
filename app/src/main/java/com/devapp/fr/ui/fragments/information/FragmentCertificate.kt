package com.devapp.fr.ui.fragments.information

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devapp.fr.adapters.RadioAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentCertificateBinding
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.util.DataHelper.getListCertificate
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentCertificate : BaseFragment<FragmentCertificateBinding>() {
    val TAG = "FragmentCertificate"
    private lateinit var radioAdapter: RadioAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val args:FragmentCertificateArgs by navArgs()

    override fun onSetupView() {
        var listSubmit = getListCertificate()
        if (args.selected!=-1) {
            listSubmit = getListCertificate().also { it[args.selected].isChecked = true }
        }
        radioAdapter = RadioAdapter {
                index,isChecked->
            Log.d(TAG, "onSetupView: $index")
            sharedViewModel.setSharedFlowCertificate(index)
        }
        radioAdapter.submitList(listSubmit)
        binding.rcvCertificate.apply {
            itemAnimator = null
            adapter = radioAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (args.isSingleNavigate) binding.ibBack.toVisible()

        binding.ibBack.setOnClickWithAnimationListener {
            findNavController().popBackStack()
        }
        super.onViewCreated(view, savedInstanceState)
    }


}