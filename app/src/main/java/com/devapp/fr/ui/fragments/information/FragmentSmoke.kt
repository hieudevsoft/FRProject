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
import com.devapp.fr.databinding.FragmentSmokeBinding

class FragmentSmoke : BaseFragment<FragmentSmokeBinding>() {
    val TAG = "FragmentSmoke"
    private lateinit var radioAdapter: RadioAdapter
    override fun onSetupView() {
        radioAdapter = RadioAdapter {
                index,isChecked->
            Log.d(TAG, "onSetupView: $index,$isChecked")
        }
        binding.rcvSmoke.apply {
            itemAnimator = null
            adapter = radioAdapter
        }
        radioAdapter.submitList(getListSmoke())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getListSmoke() = listOf(
        RadioItem("Có"),
        RadioItem("Không"),
        RadioItem("Thỉnh thoảng"),
        RadioItem("Tôi không muốn nói"),
        )
}