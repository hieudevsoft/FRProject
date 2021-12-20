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
import com.devapp.fr.databinding.FragmentEmailBinding
import com.devapp.fr.ui.activities.ConfigProfileActivity
import com.devapp.fr.ui.activities.MainActivity
import com.devapp.fr.util.UiHelper.enableOrNot
import com.devapp.fr.util.UiHelper.toGone
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.Validation
import com.devapp.fr.util.animations.AnimationHelper.startAnimClick
import com.devapp.fr.util.storages.SharedPreferencesHelper


class FragmentEmail : BaseFragment<FragmentEmailBinding>() {
    val TAG = "FragmentEmail"
    private lateinit var pref:SharedPreferencesHelper
    override fun onSetupView() {
        binding.btnContinue.enableOrNot(false)
        pref = (requireActivity() as ConfigProfileActivity).sharedPrefs
        if(pref.readEmail()?.isNotEmpty() == true) binding.edtEmail.setText(pref.readEmail())
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
        binding.btnContinue.setOnClickListener {
            pref.saveEmail(binding.edtEmail.text.toString().trim())
            it.startAnimClick()
            findNavController().navigate(FragmentEmailDirections.actionFragmentEmailToFragmentPassword())
        }
        super.onViewCreated(view, savedInstanceState)
    }

}