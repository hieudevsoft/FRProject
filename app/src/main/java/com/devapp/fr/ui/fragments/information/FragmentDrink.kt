package com.devapp.fr.ui.fragments.information

import android.os.Bundle
import android.util.Log
import android.view.View
import com.devapp.fr.adapters.RadioAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.models.items.RadioItem
import com.devapp.fr.databinding.FragmentDrinkBinding

class FragmentDrink : BaseFragment<FragmentDrinkBinding>() {
    val TAG = "FragmentDrink"
    private lateinit var radioAdapter: RadioAdapter
    override fun onSetupView() {
        radioAdapter = RadioAdapter {
                index,isChecked->
            Log.d(TAG, "onSetupView: $index,$isChecked")
        }
        binding.rcvDrink.apply {
            itemAnimator = null
            adapter = radioAdapter
        }
        radioAdapter.submitList(getListGender())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getListGender() = listOf(
        RadioItem("Có"),
        RadioItem("Không"),
        RadioItem("Thỉnh thoảng"),
        RadioItem("Tôi không muốn nói"),

    )
}