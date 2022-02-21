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
import com.devapp.fr.databinding.FragmentIntroduceBinding
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.viewmodels.AuthAndProfileViewModel
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.ui.widgets.LoadingDialog
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenResumed
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenStarted
import com.devapp.fr.util.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@AndroidEntryPoint
class FragmentIntroduce : BaseFragment<FragmentIntroduceBinding>() {
    private val args:FragmentIntroduceArgs by navArgs()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val authAndProfileViewModel: AuthAndProfileViewModel by activityViewModels()
    override fun onSetupView() {
        subscribeObserver()
        binding.edtIntroduce.addTextChangedListener {
            val numberOfCharacter = it.toString().length
            binding.tvNumberCharacter.text = "${500-numberOfCharacter} ký tự"
            if(numberOfCharacter>500){
                binding.edtIntroduce.setText(it.toString())
            }
        }
        launchRepeatOnLifeCycleWhenResumed {
            sharedViewModel.getSharedFlowIntroduce()
                .distinctUntilChanged()
                .collectLatest {
                    if(it.isNotEmpty()) binding.edtIntroduce.setText(it)
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(args.isSingleNavigate) binding.ibBack.toVisible()
        binding.apply {
            ibBack.setOnClickWithAnimationListener {
                authAndProfileViewModel.updateByFiledName(args.id,"bio",edtIntroduce.text.toString().trim())
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
                        sharedViewModel.setSharedFlowIntroduce(binding.edtIntroduce.text.toString().trim())
                        showToast("Cập nhật thành công ~")
                        findNavController().popBackStack()
                    }

                    is ResourceRemote.Error -> {
                        showToast("Có lỗi xảy ra ~")
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