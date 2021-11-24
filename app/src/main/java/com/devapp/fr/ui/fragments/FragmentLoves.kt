package com.devapp.fr.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.devapp.fr.R
import com.devapp.fr.databinding.FragmentChatsBinding
import com.devapp.fr.databinding.FragmentLovesBinding
import com.devapp.fr.databinding.FragmentSettingsBinding

class FragmentLoves : Fragment(R.layout.fragment_loves) {
    val TAG = "FragmentChats"
    private var _binding: FragmentLovesBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLovesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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