package com.devapp.fr.ui.fragments.configProfile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.devapp.fr.R
import com.devapp.fr.databinding.FragmentLovesBinding
import com.devapp.fr.databinding.FragmentProcessConfigBinding
import com.devapp.fr.ui.activities.ConfigProfileActivity
import com.devapp.fr.ui.activities.MainActivity
import com.devapp.fr.util.UiHelper.findOnClickListener
import com.devapp.fr.util.animations.AnimationHelper
import com.devapp.fr.util.animations.AnimationHelper.startAnimClick
import com.devapp.fr.util.storages.SharedPreferencesHelper


class FragmentProcessConfig : Fragment(R.layout.fragment_process_config) {
    val TAG = "FragmentChats"
    private var _binding: FragmentProcessConfigBinding? = null
    private lateinit var pref:SharedPreferencesHelper
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProcessConfigBinding.inflate(inflater)
        pref = (requireActivity() as MainActivity).getSharedPref()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //setup init view
        findOnClickListener(binding.tvFemale,binding.tvFemale){
            this.startAnimClick()
            var data =""
            data = if(this == binding.tvMale) "male" else "female"
            pref.saveGender(if(data.contains("male")) 1 else 0)
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