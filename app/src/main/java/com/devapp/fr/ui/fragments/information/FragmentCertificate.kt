package com.devapp.fr.ui.fragments.information

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devapp.fr.R
import com.devapp.fr.adapters.RadioAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentCertificateBinding
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.viewmodels.AuthAndProfileViewModel
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.ui.widgets.LoadingDialog
import com.devapp.fr.util.DataHelper.getListCertificate
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenStarted
import com.devapp.fr.util.extensions.showToast
import com.devapp.fr.util.storages.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class FragmentCertificate : BaseFragment<FragmentCertificateBinding>() {
    val TAG = "FragmentCertificate"
    private lateinit var radioAdapter: RadioAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val args:FragmentCertificateArgs by navArgs()
    private val authAndProfileViewModel: AuthAndProfileViewModel by activityViewModels()

    private var currentPositionChoose = -1
    @Inject
    lateinit var prefs: SharedPreferencesHelper
    override fun onSetupView() {

        subscribeObserver()
        var listSubmit = getListCertificate()
        if (args.selected!=-1) {
            listSubmit = getListCertificate().also { it[args.selected].isChecked = true }
        }
        radioAdapter = RadioAdapter {
                index,isChecked->
            currentPositionChoose = index
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
            authAndProfileViewModel.updateAdditionalInformation(prefs.readIdUserLogin()!!,"certificate",currentPositionChoose)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun subscribeObserver() {
        launchRepeatOnLifeCycleWhenStarted {
            authAndProfileViewModel.stateAdditionalInformation.collect {
                when (it) {
                    is ResourceRemote.Loading -> {
                        loadingDialog.show()
                    }

                    is ResourceRemote.Success -> {
                        loadingDialog.dismiss()
                        sharedViewModel.setSharedFlowCertificate(currentPositionChoose)
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
        authAndProfileViewModel.resetStateAdditionalInformation()
        super.onDestroyView()
    }

}