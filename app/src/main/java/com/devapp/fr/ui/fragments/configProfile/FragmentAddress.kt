package com.devapp.fr.ui.fragments.configProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.devapp.fr.R
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentAdressBinding
import com.devapp.fr.ui.activities.ConfigProfileActivity
import com.devapp.fr.util.UiHelper.enableOrNot
import com.devapp.fr.util.UiHelper.getStringText

class FragmentAddress : BaseFragment<FragmentAdressBinding>() {
    override fun onSetupView() {
        binding.apply {
            edtAddress.setText((requireActivity() as ConfigProfileActivity).sharedPrefs.readAddress())
            edtAddress.addTextChangedListener {
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
            (requireActivity() as ConfigProfileActivity).sharedPrefs.saveAddress(binding.edtAddress.getStringText())
            findNavController().navigate(FragmentAddressDirections.actionFragmentAddressToFragmentChooseImage())
        }
        super.onViewCreated(view, savedInstanceState)
    }
}