package com.devapp.fr.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.devapp.fr.R
import com.devapp.fr.adapters.ChatsMessageAdapter
import com.devapp.fr.data.models.items.Message
import com.devapp.fr.databinding.FragmentChatsBinding
import com.devapp.fr.databinding.FragmentSettingsBinding

class FragmentChats : Fragment(R.layout.fragment_chats) {
    val TAG = "FragmentChats"
    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatsBinding.inflate(inflater)
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