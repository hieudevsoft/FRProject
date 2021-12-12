package com.devapp.fr.ui.fragments.configProfile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.devapp.fr.R
import com.devapp.fr.databinding.FragmentLovesBinding
import com.devapp.fr.databinding.FragmentProcessConfigBinding
import com.devapp.fr.ui.activities.ConfigProfileActivity
import com.devapp.fr.util.UiHelper.findOnClickListener


class FragmentProcessConfig : Fragment(R.layout.fragment_process_config) {
    val TAG = "FragmentChats"
    private var _binding: FragmentProcessConfigBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProcessConfigBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        findOnClickListener(binding.tvFemale,binding.tvFemale){
            var data =""
            if(this == binding.tvMale) data = "male" else data = "female"
            startActivity(Intent(requireActivity(),ConfigProfileActivity::class.java).apply {
                putExtra("gender",data)
            })
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        super.onDestroyView()
    }
}