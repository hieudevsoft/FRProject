package com.devapp.fr.ui.fragments.information

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devapp.fr.R
import com.devapp.fr.adapters.RadioAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentDrinkBinding
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.viewmodels.AuthAndProfileViewModel
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.ui.widgets.CustomDialog
import com.devapp.fr.util.DataHelper.getListDrink
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenStarted
import com.devapp.fr.util.extensions.showToast
import com.devapp.fr.util.storages.SharedPreferencesHelper
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class FragmentDrink : BaseFragment<FragmentDrinkBinding>() {
    val TAG = "FragmentDrink"
    private lateinit var radioAdapter: RadioAdapter
    private val args: FragmentDrinkArgs by navArgs()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val authAndProfileViewModel: AuthAndProfileViewModel by activityViewModels()
    private lateinit var dialogLoading: CustomDialog
    private var currentPositionChoose = -1
    @Inject
    lateinit var prefs: SharedPreferencesHelper
    override fun onSetupView() {
        dialogLoading = CustomDialog(R.layout.dialog_loading)
        subscribeObserver()
        radioAdapter = RadioAdapter { index, _ ->
            currentPositionChoose = index
        }
        binding.rcvDrink.apply {
            itemAnimator = null
            adapter = radioAdapter
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (args.isSingleNavigate) binding.ibBack.toVisible()
        if (args.selected!=-1) {
            val listSubmit = getListDrink().also { it[args.selected].isChecked = true }
            radioAdapter.submitList(listSubmit)
        } else radioAdapter.submitList(getListDrink())

        binding.ibBack.setOnClickWithAnimationListener {
            authAndProfileViewModel.updateAdditionalInformation(prefs.readIdUserLogin()!!,"drink",currentPositionChoose)

        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun subscribeObserver() {
        launchRepeatOnLifeCycleWhenStarted {
            authAndProfileViewModel.stateAdditionalInformation.collect {
                when (it) {
                    is ResourceRemote.Loading -> {
                        dialogLoading.show(childFragmentManager,dialogLoading.tag)
                    }

                    is ResourceRemote.Success -> {
                        dialogLoading.dismiss()
                        sharedViewModel.setSharedFlowDrink(currentPositionChoose)
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