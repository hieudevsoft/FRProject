package com.devapp.fr.ui.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.devapp.fr.R
import com.devapp.fr.adapters.RadioAdapter
import com.devapp.fr.databinding.LayoutDialogSexualityBinding
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.viewmodels.AuthAndProfileViewModel
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.util.DataHelper
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenResumed
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenStarted
import com.devapp.fr.util.extensions.showToast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@AndroidEntryPoint
open class SexualityBottomDialogFragment :BottomSheetDialogFragment() {
    val TAG = "SexualityBottomDialogFragment"
    private lateinit var radioAdapter: RadioAdapter
    private val authAndProfileViewModel: AuthAndProfileViewModel by activityViewModels()
    private val sharedViewModel:SharedViewModel by activityViewModels()

    private var currentPostion = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style. AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_dialog_sexuality, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        subscribeObserver()
        val binding = getView()?.let { LayoutDialogSexualityBinding.bind(it) }
        radioAdapter = RadioAdapter { index, _ ->
            currentPostion=index
            authAndProfileViewModel.updateByFiledName(tag!!,"purpose",index)

        }
        radioAdapter.submitList(DataHelper.getListSexuality())
        binding?.apply {
            rcvSexuality.apply {
                itemAnimator=null
                adapter = radioAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
        launchRepeatOnLifeCycleWhenResumed {
            sharedViewModel.getSharedFlowSexuality()
                .distinctUntilChanged()
                .collectLatest {
                    try {
                        radioAdapter.setItemChecked(it)
                    }catch (e:Exception){

                    }
                }
        }
        super.onViewCreated(view, savedInstanceState)
    }


    private fun subscribeObserver() {
        launchRepeatOnLifeCycleWhenStarted {
            authAndProfileViewModel.stateFieldName.collect {
                when (it) {
                    is ResourceRemote.Loading -> {
                    }

                    is ResourceRemote.Success -> {
                        sharedViewModel.setSharedFlowSexuality(currentPostion)
                        showToast("Cập nhật thành công ~")
                    }

                    is ResourceRemote.Error -> {
                        showToast("Có lỗi xảy ra ~")
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