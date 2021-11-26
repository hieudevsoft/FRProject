package com.devapp.fr.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.EditorInfo.IME_ACTION_SEND
import androidx.core.view.inputmethod.EditorInfoCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.devapp.fr.R
import com.devapp.fr.adapters.ChatsMessageAdapter
import com.devapp.fr.data.models.items.Message
import com.devapp.fr.databinding.FragmentChatsBinding
import com.devapp.fr.databinding.FragmentInboxBinding
import com.devapp.fr.databinding.FragmentLovesBinding
import com.devapp.fr.databinding.FragmentSettingsBinding
import com.devapp.fr.util.UiHelper.GONE
import com.devapp.fr.util.UiHelper.VISIBLE
import com.devapp.fr.util.UiHelper.hideKeyboard
import com.devapp.fr.util.UiHelper.multilineIme
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import jp.wasabeef.recyclerview.animators.SlideInDownAnimator
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import java.util.*

class FragmentInbox : Fragment(R.layout.fragment_inbox) {
    val TAG = "FragmentInbox"
    private var _binding: FragmentInboxBinding? = null
    private val binding get() = _binding!!
    private var isVoice = false
    private lateinit var chatsMessageAdapter: ChatsMessageAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInboxBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Initialize RecyclerView
        initRecyclerView()

        //Init view
        initView()

        //Handle event
        handleEventElements()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView() {
        binding.apply {

        }
    }

    private fun handleEventElements() {
        binding.apply {
            ibVoiceTemp.setOnClickListener {
                Log.d(TAG, "handleEventElements: Click")
                pulIbVoice.apply {
                    it.GONE()
                    VISIBLE()
                    start()
                }
            }

            ibVoice.setOnClickListener {
                pulIbVoice.apply {
                    stop()
                    GONE()
                    ibVoiceTemp.VISIBLE()
                }
            }

            edtSendMessage.multilineIme(IME_ACTION_SEND){
                handleSendMessage()
            }

            cardSendMessage.setOnClickListener {
                handleSendMessage()
            }

            recyclerViewChat.scrollToPosition(chatsMessageAdapter.itemCount-1)
        }
    }

    private fun handleSendMessage() {
        val content = binding.edtSendMessage.text.toString()
        if(content.isNotEmpty()){
            isVoice=!isVoice
            val list = chatsMessageAdapter.differ.currentList.toMutableList()
            list.add(Message(id=UUID.randomUUID().toString(),message = content,isMe = isVoice))
            chatsMessageAdapter.submitList(list.toList())
            binding.edtSendMessage.apply {
                hideKeyboard()
                clearFocus()
                setText("")
            }
            binding.recyclerViewChat.smoothScrollToPosition(chatsMessageAdapter.differ.currentList.size)
        }
    }

    private fun initRecyclerView() {
        chatsMessageAdapter = ChatsMessageAdapter()
        chatsMessageAdapter.submitList(fakeListMessage().toList())
        binding.recyclerViewChat.apply {
            adapter = ScaleInAnimationAdapter(chatsMessageAdapter).apply {
                setDuration(1000)
                setInterpolator(OvershootInterpolator())
                setFirstOnly(false)
            }
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        }
    }

    override fun onDestroy() {

        super.onDestroy()
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        super.onDestroyView()
        _binding = null
    }
    private fun fakeListMessage():MutableList<Message>{
        return mutableListOf(
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