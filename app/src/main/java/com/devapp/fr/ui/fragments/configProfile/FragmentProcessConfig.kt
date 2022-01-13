package com.devapp.fr.ui.fragments.configProfile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.devapp.fr.R
import com.devapp.fr.databinding.FragmentProcessConfigBinding
import com.devapp.fr.ui.activities.ConfigProfileActivity
import com.devapp.fr.util.UiHelper.findOnClickListener
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
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pref = (requireActivity() as ConfigProfileActivity).sharedPrefs
        //setup init view
        findOnClickListener(binding.tvMale,binding.tvFemale){
            this.startAnimClick()
            when(this){
                binding.tvFemale->{
                    pref.saveGender(0)
                }
                binding.tvMale->{
                    pref.saveGender(1)
                }
            }
            findNavController().navigate(FragmentProcessConfigDirections.actionFragmentProcessConfigToFragmentNickName())
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