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
import com.devapp.fr.data.models.RadioModel
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

    fun getListGender() = listOf(
        RadioModel("Có"),
        RadioModel("Không"),
        RadioModel("Thỉnh thoảng"),
        RadioModel("Tôi không muốn nói"),

    )
}