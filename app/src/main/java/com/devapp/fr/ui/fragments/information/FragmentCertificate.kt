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

class FragmentCertificate : BaseFragment<FragmentCertificateBinding>() {
    val TAG = "FragmentCertificate"
    private lateinit var radioAdapter: RadioAdapter
    override fun onSetupView() {
        radioAdapter = RadioAdapter {
                index,isChecked->
            Log.d(TAG, "onSetupView: $index,$isChecked")
        }
        binding.rcvCertificate.apply {
            itemAnimator = null
            adapter = radioAdapter
        }
        radioAdapter.submitList(getListCertificate())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getListCertificate() = listOf(
        RadioItem("Trường trung học"),
        RadioItem("Thạc sĩ hoặc cao hơn"),
        RadioItem("Trong trường đại học"),
        RadioItem("Trong trường cao đẳng"),
        RadioItem("Bằng đại học"),
        RadioItem("Tôi không muốn nói"),

        )
}