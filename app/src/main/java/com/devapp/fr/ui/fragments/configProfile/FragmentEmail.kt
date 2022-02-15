package com.devapp.fr.ui.fragments.configProfile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.devapp.fr.R
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentEmailBinding
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.activities.ConfigProfileActivity
import com.devapp.fr.ui.viewmodels.AuthAndProfileViewModel
import com.devapp.fr.ui.widgets.CustomDialog
import com.devapp.fr.util.UiHelper.enableOrNot
import com.devapp.fr.util.UiHelper.showSnackbar
import com.devapp.fr.util.UiHelper.toGone
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.Validation
import com.devapp.fr.util.animations.AnimationHelper.startAnimClick
import com.devapp.fr.util.storages.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FragmentEmail : BaseFragment<FragmentEmailBinding>() {
    val TAG = "FragmentEmail"
    private lateinit var pref:SharedPreferencesHelper
    private val authViewModel: AuthAndProfileViewModel by viewModels()
    private lateinit var dialogLoading: CustomDialog

    override fun onAttach(context: Context) {
        dialogLoading = CustomDialog(R.layout.dialog_loading)
        super.onAttach(context)
    }

    override fun onSetupView() {
        pref = (requireActivity() as ConfigProfileActivity).sharedPrefs
        if(pref.readEmail()?.isNotEmpty() == true) binding.edtEmail.setText(pref.readEmail())
        pref.readEmail()?.let { binding.btnContinue.enableOrNot(it.isNotEmpty()) }
        binding.edtEmail.addTextChangedListener {
            val text = it.toString()
            if(text.isEmpty()){
                binding.errorEmail.apply {
                    toVisible()
                    setText("Email không được để trống")
                    binding.btnContinue.enableOrNot(false)
                }
            } else if(!Validation.validateEmailField(text)){
                binding.errorEmail.apply {
                    toVisible()
                    setText("Email không hợp lệ ")
                    binding.btnContinue.enableOrNot(false)
                }
            } else {
                binding.btnContinue.enableOrNot(true)
                binding.errorEmail.toGone()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subscriberObserver()
        binding.btnContinue.setOnClickListener {
            authViewModel.isEmailExits(binding.edtEmail.text.toString().trim())
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun subscriberObserver() {
        lifecycleScope.launchWhenResumed {
            authViewModel.stateEmailExist.collectLatest {
                when(it){
                    is ResourceRemote.Loading->{
                        dialogLoading.show(childFragmentManager,dialogLoading.tag)
                    }

                    is ResourceRemote.Success->{
                        binding.root.showSnackbar("Email đã tồn tại!")
                        dialogLoading.dismiss()
                    }

                    is ResourceRemote.Error->{
                        dialogLoading.dismiss()
                        Log.d(TAG, "subscriberObserver: ${it.message}")
                    }

                    is ResourceRemote.Empty->{
                        pref.saveEmail(binding.edtEmail.text.toString().trim())
                        pref.saveProcessRegister(4)
                        binding.btnContinue.startAnimClick()
                        findNavController().navigate(FragmentEmailDirections.actionFragmentEmailToFragmentPassword())
                    }

                    is ResourceRemote.Idle->{

                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        authViewModel.resetStateEmailExist()
        super.onDestroyView()
    }
}