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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.devapp.fr.R
import com.devapp.fr.adapters.ChatsMessageAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.models.MessageType
import com.devapp.fr.data.models.messages.MessageAudio
import com.devapp.fr.data.models.messages.MessageImage
import com.devapp.fr.data.models.messages.MessageModel
import com.devapp.fr.data.models.messages.MessageText
import com.devapp.fr.databinding.FragmentInboxBinding
import com.devapp.fr.ui.activities.FullScreenImageActivity
import com.devapp.fr.util.Constants.RC_MEDIA
import com.devapp.fr.util.MediaHelper
import com.devapp.fr.util.PermissionHelper
import com.devapp.fr.util.UiHelper.GONE
import com.devapp.fr.util.UiHelper.VISIBLE
import com.devapp.fr.util.UiHelper.hideKeyboard
import com.devapp.fr.util.UiHelper.multilineIme
import com.devapp.fr.util.UiHelper.showSnackbar
import gun0912.tedbottompicker.TedBottomPicker
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*


class FragmentInbox : BaseFragment<FragmentInboxBinding>(), EasyPermissions.PermissionCallbacks {
    val TAG = "FragmentInbox"
    private lateinit var chatsMessageAdapter: ChatsMessageAdapter

    override fun onSetupView() {

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
                pulIbVoice.apply {
                    it.GONE()
                    VISIBLE()
                    start()
                }
                if(PermissionHelper.hasPermissionAudio(requireContext())) startRecording()
                else PermissionHelper.requestPermissionAudio(this@FragmentInbox)
            }

            ibVoice.setOnClickListener {
                pulIbVoice.apply {
                    stop()
                    GONE()
                    ibVoiceTemp.VISIBLE()
                }
                stopRecording()
            }

            edtSendMessage.multilineIme(IME_ACTION_SEND) {
                handleSendMessage()
            }

            cardSendMessage.setOnClickListener {
                handleSendMessage()
            }

            ibCamera.setOnClickListener {
                if(PermissionHelper.hasPermissionBottomPicker(requireContext())) openBottomImagePicker() else
                PermissionHelper.requestPermissionBottomPicker(this@FragmentInbox)
            }
        }
    }

    private fun startRecording() {
        MediaHelper.startRecording(requireContext())
    }

    private fun stopRecording(){
        MediaHelper.stopRecording(requireContext())
        val newVoice = MessageAudio("111","111",MessageType.AUDIO,false,MediaHelper.mFileName)
        val list = chatsMessageAdapter.differ.currentList.toMutableList()
        list.add(newVoice)
        chatsMessageAdapter.submitList(list)
        binding.recyclerViewChat.smoothScrollToPosition(binding.recyclerViewChat.adapter!!.itemCount+1)
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
                    isMe = true,
                    type = MessageType.TEXT
                )
            )
            chatsMessageAdapter.submitList(list.toList())
            binding.edtSendMessage.apply {
                hideKeyboard()
                clearFocus()
                setText("")
            }
            binding.recyclerViewChat.smoothScrollToPosition(chatsMessageAdapter.differ.currentList.size+1)
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
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false).apply {
                    stackFromEnd = true
                }
            scrollToPosition(chatsMessageAdapter.itemCount+1)
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
        Log.d(TAG, "initRecyclerView: ${chatsMessageAdapter.differ.currentList.size}")
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
            MessageText(id = "11", message = "HI!", isMe = true, type = MessageType.TEXT),
            MessageText(id = "12", message = "HI!", isMe = false, type = MessageType.TEXT),
            MessageText(id = "12", message = "HI!", isMe = false, type = MessageType.TEXT),
            MessageText(id = "8", message = "UKM", isMe = true, type = MessageType.TEXT),
            MessageText(id = "111", message = "BYE", isMe = false, type = MessageType.TEXT),
            MessageText(id = "21", message = "HI!", isMe = false, type = MessageType.TEXT),
            MessageText(id = "321", message = "HI!", isMe = true, type = MessageType.TEXT),
            MessageText(id = "321", message = "HI!", isMe = false, type = MessageType.TEXT),
            MessageText(id = "321", message = "HI!", isMe = false, type = MessageType.TEXT),
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if(requestCode == RC_MEDIA)  openBottomImagePicker() else startRecording()
    }

    private fun openBottomImagePicker() {
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
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            if(requestCode==RC_MEDIA)
            PermissionHelper.requestPermissionBottomPicker(this)
            else PermissionHelper.requestPermissionAudio(this)
        }
    }


}