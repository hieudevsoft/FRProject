package com.devapp.fr.ui.fragments.information

import android.os.Bundle
import android.util.Log
import android.view.View
import com.devapp.fr.adapters.RadioAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.models.items.RadioItem
import com.devapp.fr.databinding.FragmentMaritalStatusBinding

class FragmentMaritalStatus : BaseFragment<FragmentMaritalStatusBinding>() {
    val TAG = "FragmentMaritalStatus"
    private lateinit var radioAdapter: RadioAdapter
    override fun onSetupView() {
        radioAdapter = RadioAdapter {
                index,isChecked->
            Log.d(TAG, "onSetupView: $index,$isChecked")
        }
        binding.rcvSt.apply {
            itemAnimator = null
            adapter = radioAdapter
        }
        radioAdapter.submitList(getListGender())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun getListGender() = listOf(
        RadioItem("Độc thân"),
        RadioItem("Đang hẹn hò"),
        RadioItem("Phức tạp"),
        RadioItem("Đang mở lòng"),
        RadioItem("Tôi không muốn nói"),
    )
}