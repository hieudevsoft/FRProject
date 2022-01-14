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
import com.devapp.fr.databinding.FragmentPetBinding
import com.devapp.fr.databinding.FragmentReligionBinding

class FragmentReligion : BaseFragment<FragmentReligionBinding>() {
    val TAG = "FragmentReligion"
    private lateinit var radioAdapter: RadioAdapter
    override fun onSetupView() {
        radioAdapter = RadioAdapter {
                index,isChecked->
            Log.d(TAG, "onSetupView: $index,$isChecked")
        }
        binding.rcvReligion.apply {
            itemAnimator = null
            adapter = radioAdapter
        }
        radioAdapter.submitList(getListReligion())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getListReligion() = listOf(
        RadioItem("Bất khả tri"),
        RadioItem("Vô thần"),
        RadioItem("Đạo phật"),
        RadioItem("Công giáo"),
        RadioItem("Cơ đốc giáo"),
        RadioItem("Hindu"),
        RadioItem("Do thái"),
        RadioItem("Mặc Môn"),
        RadioItem("Hồi giáo"),
        RadioItem("Hỏa giáo"),
        RadioItem("Sikh"),
        RadioItem("Tâm linh"),
        RadioItem("Khác"),
        RadioItem("Tôi không muốn nói"),

        )
}