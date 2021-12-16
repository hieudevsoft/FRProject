package com.devapp.fr.ui.fragments.configProfile

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.devapp.fr.R
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentNickNameBinding
import com.devapp.fr.ui.activities.ConfigProfileActivity
import com.devapp.fr.util.UiHelper.enableOrNot

class FragmentNickName : BaseFragment<FragmentNickNameBinding>() {
    override fun onSetupView() {
        binding.apply {
            edtName.setText((requireActivity() as ConfigProfileActivity).sharedPrefs.readNameConfig())
            edtName.addTextChangedListener {
                if(it.toString().isEmpty()){
                    btnContinue.enableOrNot(false)
                }else{
                    btnContinue.enableOrNot(true)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnContinue.setOnClickListener {
            val data = binding.edtName.text.toString()
            findNavController().navigate(FragmentNickNameDirections.actionFragmentNickNameToFragmentDateOfBirth(data))
            (requireActivity() as ConfigProfileActivity).sharedPrefs.saveNameConfig(data)
        }
        super.onViewCreated(view, savedInstanceState)
    }
}