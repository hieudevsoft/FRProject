package com.devapp.fr.ui.fragments.information

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devapp.fr.R
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentUserJobBinding
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.viewmodels.AuthAndProfileViewModel
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.ui.widgets.LoadingDialog
import com.devapp.fr.util.UiHelper.showSnackbar
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenResumed
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenStarted
import com.devapp.fr.util.extensions.showToast
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

class FragmentUserJob : BaseFragment<FragmentUserJobBinding>() {
    private val args:FragmentUserJobArgs by navArgs()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val authAndProfileViewModel: AuthAndProfileViewModel by activityViewModels()

    override fun onSetupView() {

        subscribeObserver()
        launchRepeatOnLifeCycleWhenResumed {
            sharedViewModel.getSharedFlowJob()
                .distinctUntilChanged()
                .collectLatest {
                    if(it.isNotEmpty()) binding.edtCompany.setText(it)
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(args.isSingleNavigate) binding.ibBack.toVisible()
        binding.apply {
            ibBack.setOnClickWithAnimationListener {
                authAndProfileViewModel.updateByFiledName(args.id,"job",edtCompany.text.toString().trim())
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun subscribeObserver() {
        launchRepeatOnLifeCycleWhenStarted {
            authAndProfileViewModel.stateFieldName.collect {
                when (it) {
                    is ResourceRemote.Loading -> {
                        loadingDialog.show()
                    }

                    is ResourceRemote.Success -> {
                        loadingDialog.dismiss()
                        sharedViewModel.setSharedFlowJob(binding.edtCompany.text.toString().trim())
                        showToast("C???p nh???t th??nh c??ng ~")
                        findNavController().popBackStack()
                    }

                    is ResourceRemote.Error -> {
                        showToast("C?? l???i x???y ra ~")
                        findNavController().popBackStack()
                    }
                    else -> {

                    }
                }

            }
        }
    }

    override fun onDestroyView() {
        authAndProfileViewModel.resetStateFieldName()
        super.onDestroyView()
    }

}