package com.devapp.fr.ui.fragments.information

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devapp.fr.R
import com.devapp.fr.adapters.RadioAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.models.RadioModel
import com.devapp.fr.databinding.FragmentChooseGenderBinding

class FragmentChooseGender : BaseFragment<FragmentChooseGenderBinding>() {
    private lateinit var radioAdapter:RadioAdapter
    override fun onSetupView() {
        radioAdapter = RadioAdapter {

        }
        binding.rcvGender.apply {
            itemAnimator = null
            adapter = radioAdapter
        }
        radioAdapter.submitList(listOf(RadioModel("hieu ne"),RadioModel("sao nao")))

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}