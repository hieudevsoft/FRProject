package com.devapp.fr.ui.fragments.information

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devapp.fr.R
import com.devapp.fr.adapters.RadioAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.models.items.RadioItem
import com.devapp.fr.databinding.FragmentCertificateBinding
import com.devapp.fr.databinding.FragmentPetBinding

class FragmentPet : BaseFragment<FragmentPetBinding>() {
    val TAG = "FragmentPet"
    private lateinit var radioAdapter: RadioAdapter
    override fun onSetupView() {
        radioAdapter = RadioAdapter {
                index,isChecked->
            Log.d(TAG, "onSetupView: $index,$isChecked")
        }
        binding.rcvPet.apply {
            itemAnimator = null
            adapter = radioAdapter
        }
        radioAdapter.submitList(getListPet())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getListPet() = listOf(
        RadioItem("Mèo"),
        RadioItem("Chó"),
        RadioItem("Cả mèo và chó"),
        RadioItem("Động vật khác"),
        RadioItem("Không thú cưng"),
        RadioItem("Tôi không muốn nói"),

        )
}