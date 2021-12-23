package com.devapp.fr.ui.fragments.configProfile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.devapp.fr.R
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentPasswordBinding
import com.devapp.fr.ui.activities.ConfigProfileActivity
import com.devapp.fr.util.UiHelper.enableOrNot
import com.devapp.fr.util.UiHelper.toGone
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.Validation
import com.devapp.fr.util.animations.AnimationHelper.startAnimClick
import com.devapp.fr.util.storages.SharedPreferencesHelper

class FragmentPassword : BaseFragment<FragmentPasswordBinding>() {
    private lateinit var pref:SharedPreferencesHelper
    override fun onSetupView() {
        pref = (requireActivity() as ConfigProfileActivity).sharedPrefs
        binding.btnContinue.enableOrNot(pref.readPassword().isNullOrEmpty())
        binding.edtPassword.addTextChangedListener {
            val text = it.toString()
            if(text.isEmpty()){
                binding.errorPassWord.apply {
                    toVisible()
                    setText("Password không được để trống")
                    binding.btnContinue.enableOrNot(false)
                }
            } else if(!Validation.validatePasswordField(text)){
                binding.errorPassWord.apply {
                    toVisible()
                    setText("Password phải ít nhát 8 kí tự")
                    binding.btnContinue.enableOrNot(false)
                }
            } else {
                binding.btnContinue.enableOrNot(true)
                binding.errorPassWord.toGone()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnContinue.setOnClickListener {
            pref.savePassword(binding.edtPassword.text.toString().trim())
            pref.saveIsLogin(false)
            it.startAnimClick()
            findNavController().navigate(FragmentPasswordDirections.actionFragmentPasswordToFragmentImage())
        }
        super.onViewCreated(view, savedInstanceState)
    }

}