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
    private lateinit var chatsMessageAdapter: ChatsMessageAdapter
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
        //Initialize RecyclerView
        initRecyclerView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initRecyclerView() {
        chatsMessageAdapter = ChatsMessageAdapter()
        chatsMessageAdapter.submitList(fakeListMessage())
        binding.recyclerViewChat.apply {
            adapter = chatsMessageAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        super.onDestroyView()
    }

    private fun fakeListMessage():List<Message>{
        return listOf(
            Message(id="1",message = "Xin chào cậu tôi muốn làm quen",isMe = true),
            Message(id="2",message = "Ồ không được đâu mình có người yêu r",isMe = false),
            Message(id="3",message = "UKM",isMe = true),
            Message(id="4",message = "BYE",isMe = false),
            Message(id="5",message = "HI!",isMe = false),
            Message(id="6",message = "Xin chào cậu tôi muốn làm quen",isMe = true),
            Message(id="7",message = "Ồ không được đâu mình có người yêu r",isMe = false),
            Message(id="8",message = "UKM",isMe = true),
            Message(id="9",message = "BYE",isMe = false),
            Message(id="10",message = "HI!",isMe = false),
        )
    }
}