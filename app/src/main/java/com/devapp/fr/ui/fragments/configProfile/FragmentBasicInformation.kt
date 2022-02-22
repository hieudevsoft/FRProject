package com.devapp.fr.ui.fragments.configProfile

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devapp.fr.R
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentBasicInformationBinding
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.viewmodels.AuthAndProfileViewModel
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.ui.widgets.LoadingDialog
import com.devapp.fr.util.UiHelper.enableOrNot
import com.devapp.fr.util.UiHelper.showSnackbar
import com.devapp.fr.util.UiHelper.toGone
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.extensions.convertLongToDateVn
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenResumed
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenStarted
import com.devapp.fr.util.extensions.showToast
import com.devapp.fr.util.storages.SharedPreferencesHelper
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@AndroidEntryPoint
class FragmentBasicInformation : BaseFragment<FragmentBasicInformationBinding>() {
    val TAG = "FragmentBasicInformation"
    private lateinit var hashMap: HashMap<Int,Any>
    private val sharedViewModel:SharedViewModel by activityViewModels()
    private val authAndProfileViewModel:AuthAndProfileViewModel by activityViewModels()

    private val args:FragmentBasicInformationArgs by navArgs()
    @Inject
    lateinit var prefs:SharedPreferencesHelper
    override fun onSetupView() {

        statusBtnDone()
        setDataForFields()
        subscribeObserver()
        binding.apply {
            edtDob.setOnKeyListener(null)
            edtDob.isFocusable = false
            edtDob.setOnClickListener {
                initDatePicker()
            }

            btnBack.setOnClickListener { findNavController().popBackStack() }
            btnDone.setOnClickListener {
                hashMap = hashMapOf(
                    0 to binding.edtName.text.toString().trim(),
                    1 to binding.edtDob.text.toString().trim(),
                    2 to binding.edtAddress.text.toString().trim(),
                    3 to if(binding.male.isChecked) 1 else 0
                )
                authAndProfileViewModel.updateBasicInformation(args.id,hashMap)
            }
        }
    }

    private fun setDataForFields() {
        launchRepeatOnLifeCycleWhenResumed {
            sharedViewModel.getSharedFlowBasicInformation()
                .distinctUntilChanged()
                .collectLatest {
                    binding.apply {
                        edtName.setText(it[0] as String)
                        edtDob.setText(it[1] as String)
                        edtAddress.setText(it[2] as String)
                        val gender = (it[3] as Int)==0
                        if(gender) binding.female.isChecked = true else binding.male.isChecked = true
                    }
                }
        }
    }

    private fun statusBtnDone(){
        launchRepeatOnLifeCycleWhenResumed {
            while(true){
                if(binding.edtName.text.trim().isNotEmpty()
                        && binding.edtAddress.text.trim().isNotEmpty()
                        && (binding.male.isChecked || binding.female.isChecked))
                            binding.btnDone.toVisible() else binding.btnDone.toGone()
                delay(200)
            }
        }
    }

    private fun initDatePicker() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Chọn ngày")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                .setTheme(R.style.ThemeOverlay_App_DatePicker)
                .build()

        datePicker.show(requireActivity().supportFragmentManager, "SignupFragment DatePicker")
        datePicker.addOnPositiveButtonClickListener {
            val date = convertLongToDateVn(it)
            binding.edtDob.setText(date)
        }
    }

    private fun subscribeObserver() {
        launchRepeatOnLifeCycleWhenStarted {
            authAndProfileViewModel.stateBasicInformation.collect {
                when (it) {
                    is ResourceRemote.Loading -> {
                        loadingDialog.show()
                    }

                    is ResourceRemote.Success -> {
                        loadingDialog.dismiss()
                        sharedViewModel.setSharedFlowBasicInformation(hashMap)
                        showToast("Cập nhật thành công ~")
                        binding.root.showSnackbar("Chạy lại ứng dụng để cập nhật danh sách kết đôi nhé ~")
                        findNavController().popBackStack()
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
        authAndProfileViewModel.resetStateBasicInformation()
        super.onDestroyView()
    }
}