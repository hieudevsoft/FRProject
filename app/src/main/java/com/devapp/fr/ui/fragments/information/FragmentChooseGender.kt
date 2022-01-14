package com.devapp.fr.ui.fragments.information

import android.os.Bundle
import android.util.Log
import android.view.View
import com.devapp.fr.adapters.RadioAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.models.items.RadioItem
import com.devapp.fr.databinding.FragmentChooseGenderBinding

class FragmentChooseGender : BaseFragment<FragmentChooseGenderBinding>() {
    val TAG = "FragmentChooseGender"
    private lateinit var radioAdapter:RadioAdapter
    override fun onSetupView() {
        radioAdapter = RadioAdapter {
            index,isChecked->
            Log.d(TAG, "onSetupView: $index,$isChecked")
        }
        binding.rcvGender.apply {
            itemAnimator = null
            adapter = radioAdapter
        }
        radioAdapter.submitList(getListGender())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getListGender() = listOf(
        RadioItem("Giới tính thẳng"),
        RadioItem("Gay"),
        RadioItem("Đồng tính nữ"),
        RadioItem("Lưỡng tính"),
        RadioItem("Vô tính"),
        RadioItem("Á tính"),
        RadioItem("Toàn tính luyến ái"),
        RadioItem("Queer"),
        RadioItem("Đang tự hỏi"),
        RadioItem("Tôi không muốn nói")
    )
}