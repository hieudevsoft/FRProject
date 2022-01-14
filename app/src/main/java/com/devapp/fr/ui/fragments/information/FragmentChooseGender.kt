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

    fun getListGender() = listOf(
        RadioModel("Giới tính thẳng"),
        RadioModel("Gay"),
        RadioModel("Đồng tính nữ"),
        RadioModel("Lưỡng tính"),
        RadioModel("Vô tính"),
        RadioModel("Á tính"),
        RadioModel("Toàn tính luyến ái"),
        RadioModel("Queer"),
        RadioModel("Đang tự hỏi"),
        RadioModel("Tôi không muốn nói")
    )
}