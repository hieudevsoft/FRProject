package com.devapp.fr.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.view.inputmethod.EditorInfo.IME_ACTION_SEND
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.devapp.fr.R
import com.devapp.fr.adapters.ChatsMessageAdapter
import com.devapp.fr.data.models.MessageType
import com.devapp.fr.data.models.messages.MessageImage
import com.devapp.fr.data.models.messages.MessageModel
import com.devapp.fr.data.models.messages.MessageText
import com.devapp.fr.databinding.FragmentInboxBinding
import com.devapp.fr.ui.activities.FullScreenImageActivity
import com.devapp.fr.util.PermissionHelper
import com.devapp.fr.util.UiHelper.GONE
import com.devapp.fr.util.UiHelper.VISIBLE
import com.devapp.fr.util.UiHelper.hideKeyboard
import com.devapp.fr.util.UiHelper.multilineIme
import com.devapp.fr.util.UiHelper.showSnackbar
import gun0912.tedbottompicker.TedBottomPicker
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*


class FragmentInbox : Fragment(R.layout.fragment_inbox), EasyPermissions.PermissionCallbacks {
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

            edtSendMessage.multilineIme(IME_ACTION_SEND) {
                handleSendMessage()
            }

            cardSendMessage.setOnClickListener {
                handleSendMessage()
            }

            ibCamera.setOnClickListener {
                PermissionHelper.requestPermissionBottomPicker(this@FragmentInbox)
            }
            recyclerViewChat.scrollToPosition(chatsMessageAdapter.itemCount - 1)

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun handleSendMessage() {
        val content = binding.edtSendMessage.text.toString()
        if (content.isNotEmpty()) {
            val list = chatsMessageAdapter.differ.currentList.toMutableList()
            list.add(
                MessageText(
                    id = UUID.randomUUID().toString(),
                    message = content,
                    isMe = isVoice,
                    type = MessageType.TEXT
                )
            )
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
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        chatsMessageAdapter.setOnItemImageClickListener { view, url ->
            val intent = Intent(requireActivity(), FullScreenImageActivity::class.java)
            intent.putExtra("url", url)
            val options: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                requireActivity(),
                view,
                "image_fullscreen"
            )
            startActivity(intent, options.toBundle())
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

    private fun fakeListMessage(): MutableList<MessageModel> {
        return mutableListOf(
            MessageText(
                id = "1",
                message = "Xin chào cậu tôi muốn làm quen",
                isMe = true,
                type = MessageType.TEXT
            ),
            MessageText(
                id = "2",
                message = "Ồ không được đâu mình có người yêu r",
                isMe = false,
                type = MessageType.TEXT
            ),
            MessageText(id = "3", message = "UKM", isMe = true, type = MessageType.TEXT),
            MessageText(id = "4", message = "BYE", isMe = false, type = MessageType.TEXT),
            MessageText(id = "5", message = "HI!", isMe = false, type = MessageType.TEXT),
            MessageText(
                id = "6",
                message = "Xin chào cậu tôi muốn làm quen",
                isMe = true,
                type = MessageType.TEXT
            ),
            MessageText(
                id = "7",
                message = "Ồ không được đâu mình có người yêu r",
                isMe = false,
                type = MessageType.TEXT
            ),
            MessageText(id = "8", message = "UKM", isMe = true, type = MessageType.TEXT),
            MessageText(id = "9", message = "BYE", isMe = false, type = MessageType.TEXT),
            MessageText(id = "10", message = "HI!", isMe = false, type = MessageType.TEXT),
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (!isVoice) {
            openBottomImagePicker();
        }
    }

    private fun openBottomImagePicker() {
        if (PermissionHelper.hasPermissionBottomPicker(requireContext())) {
            val tedBottomPicker =
                TedBottomPicker.with(requireActivity())
                    .setOnMultiImageSelectedListener {
                        val list = chatsMessageAdapter.differ.currentList.toMutableList()
                        var test = false
                        it.forEach { uri ->
                            test=!test
                            list.add(
                                MessageImage(
                                    id = UUID.randomUUID().toString(),
                                    urlImage = uri.toString(),
                                    isMe = test,
                                    type = MessageType.IMAGE
                                )
                            )
                            chatsMessageAdapter.submitList(list.toList())
                            binding.recyclerViewChat.smoothScrollToPosition(binding.recyclerViewChat.adapter!!.itemCount+1)
                        }
                    }
                    .setOnErrorListener {
                        binding.root.showSnackbar(it)
                    }
                    .setTitle(R.string.select_image)
                    .setCompleteButtonText(R.string.selected_done)
                    .setEmptySelectionText("Trống")
                    .create()
            tedBottomPicker.show(childFragmentManager)
        } else {
            binding.root.showSnackbar("Need Permission")
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            PermissionHelper.requestPermissionBottomPicker(this)
        }
    }
}