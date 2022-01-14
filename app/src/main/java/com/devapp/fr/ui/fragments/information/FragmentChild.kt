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
import com.devapp.fr.databinding.FragmentChildBinding
import com.devapp.fr.databinding.FragmentDrinkBinding

class FragmentChild : BaseFragment<FragmentChildBinding>() {
    val TAG = "FragmentChild"
    private lateinit var radioAdapter: RadioAdapter
    override fun onSetupView() {
        radioAdapter = RadioAdapter {
                index,isChecked->
            Log.d(TAG, "onSetupView: $index,$isChecked")
        }
        binding.rcvChild.apply {
            itemAnimator = null
            adapter = radioAdapter
        }
        radioAdapter.submitList(getListChild())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getListChild() = listOf(
        RadioItem("Có vào một ngày nào đó"),
        RadioItem("Tôi sắp có"),
        RadioItem("Tôi không muốn có em bé"),
        RadioItem("Tôi đã có"),
        RadioItem("Tôi không muốn nói"),

        )
}