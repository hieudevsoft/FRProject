package com.devapp.fr.ui.fragments.information

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devapp.fr.R
import com.devapp.fr.adapters.RadioAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentMaritalStatusBinding
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.viewmodels.AuthAndProfileViewModel
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.ui.widgets.LoadingDialog
import com.devapp.fr.util.DataHelper.getListMaritalStatus
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenStarted
import com.devapp.fr.util.extensions.showToast
import com.devapp.fr.util.storages.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class FragmentMaritalStatus : BaseFragment<FragmentMaritalStatusBinding>() {
    val TAG = "FragmentMaritalStatus"
    private lateinit var radioAdapter: RadioAdapter
    private val args:FragmentMaritalStatusArgs by navArgs()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val authAndProfileViewModel: AuthAndProfileViewModel by activityViewModels()

    private var currentPositionChoose = -1
    @Inject
    lateinit var prefs: SharedPreferencesHelper
    override fun onSetupView() {

        subscribeObserver()
        radioAdapter = RadioAdapter {
                index, _ ->
            currentPositionChoose = index
        }
        binding.rcvSt.apply {
            itemAnimator = null
            adapter = radioAdapter
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (args.isSingleNavigate) binding.ibBack.toVisible()
        if (args.selected!=-1) {
            val listSubmit = getListMaritalStatus().also { it[args.selected].isChecked = true }
            radioAdapter.submitList(listSubmit)
        } else radioAdapter.submitList(getListMaritalStatus())


        binding.ibBack.setOnClickWithAnimationListener {
            authAndProfileViewModel.updateAdditionalInformation(prefs.readIdUserLogin()!!,"maritalStatus",currentPositionChoose)
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
                        sharedViewModel.setSharedFlowMaritalStatus(currentPositionChoose)
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
        authAndProfileViewModel.resetStateAdditionalInformation()
        super.onDestroyView()
    }
}