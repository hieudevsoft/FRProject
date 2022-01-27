package com.devapp.fr.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.devapp.fr.R
import com.devapp.fr.adapters.InformationAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.models.items.InformationItem
import com.devapp.fr.databinding.FragmentEditProfileBinding
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener

class FragmentEditProfile : BaseFragment<FragmentEditProfileBinding>() {
    private lateinit var informationAdapter: InformationAdapter

    override fun onSetupView() {
        setupRecyclerViewInformation()
    }

    private fun setupRecyclerViewInformation() {
        informationAdapter = InformationAdapter()
        binding.rcInformation.adapter = informationAdapter
        informationAdapter.submitList(getListItemInformation())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnBack.setOnClickWithAnimationListener { findNavController().popBackStack() }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getListItemInformation() = listOf(
        InformationItem(R.drawable.ic_tall,"Cao",""),
        InformationItem(R.drawable.ic_kid,"Trẻ con",""),
        InformationItem(R.drawable.ic_beer,"Rượu bia",""),
        InformationItem(R.drawable.ic_status_marry,"Về hôn nhân",""),
        InformationItem(R.drawable.ic_gender,"Giới tính",""),
        InformationItem(R.drawable.ic_smoking,"Hút thuốc",""),
        InformationItem(R.drawable.ic_pet,"Thú cưng",""),
        InformationItem(R.drawable.ic_religion,"Tôn giáo",""),
        InformationItem(R.drawable.ic_certificate,"Học vấn",""),
        InformationItem(R.drawable.ic_personality,"Tính cách",""),
    )
}