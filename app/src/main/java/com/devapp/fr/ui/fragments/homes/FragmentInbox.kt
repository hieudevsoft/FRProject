package com.devapp.fr.ui.fragments.homes

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator
import android.view.inputmethod.EditorInfo.IME_ACTION_SEND
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.devapp.fr.R
import com.devapp.fr.adapters.ChatsMessageAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.entities.MessageUpload
import com.devapp.fr.data.models.MessageType
import com.devapp.fr.data.models.messages.MessageAudio
import com.devapp.fr.data.models.messages.MessageImage
import com.devapp.fr.data.models.messages.MessageModel
import com.devapp.fr.data.models.messages.MessageText
import com.devapp.fr.databinding.FragmentInboxBinding
import com.devapp.fr.ui.viewmodels.RealTimeViewModel
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.util.Constants.RC_MEDIA
import com.devapp.fr.util.GlideApp
import com.devapp.fr.util.MediaHelper
import com.devapp.fr.util.PermissionHelper
import com.devapp.fr.util.UiHelper.GONE
import com.devapp.fr.util.UiHelper.VISIBLE
import com.devapp.fr.util.UiHelper.hideKeyboard
import com.devapp.fr.util.UiHelper.multilineIme
import com.devapp.fr.util.UiHelper.sendImageToFullScreenImageActivity
import com.devapp.fr.util.UiHelper.showSnackbar
import com.devapp.fr.util.UiHelper.toGone
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenCreated
import com.devapp.fr.util.extensions.showToast
import com.devapp.fr.util.storages.SharedPreferencesHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedbottompicker.TedBottomPicker
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.coroutines.flow.collectLatest
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class FragmentInbox() : BaseFragment<FragmentInboxBinding>(), EasyPermissions.PermissionCallbacks {
    val TAG = "FragmentInbox"
    private lateinit var chatsMessageAdapter: ChatsMessageAdapter
    private val args:FragmentInboxArgs by navArgs()
    private lateinit var listMessage:MutableList<MessageModel>
    private lateinit var database:FirebaseDatabase
    private lateinit var storage:FirebaseStorage

    @Inject
    lateinit var prefs:SharedPreferencesHelper
    private val realTimeViewModel:RealTimeViewModel by activityViewModels()
    private val sharedViewModel:SharedViewModel by activityViewModels()

    @SuppressLint("ResourceAsColor")
    override fun onSetupView() {
        initRecyclerView()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        handleOnBackPress()
        args.data?.let {
            it.images?.get(0)?.let { GlideApp.loadImage(it,binding.imgPartner, this) }
            val anim: Animation = AlphaAnimation(0.0f, 1.0f)
            anim.duration = 500
            anim.startOffset = 20
            anim.repeatMode = Animation.REVERSE
            anim.repeatCount = Animation.INFINITE
            binding.apply {
                dotOnline.startAnimation(anim)
                tvName.text = it.name
            }
            launchRepeatOnLifeCycleWhenCreated {scope->
                realTimeViewModel.checkUserOnOffbyId(it.id)
                realTimeViewModel.stateFlowUserOnOff.collectLatest {
                    if(it == true){
                        binding.apply {
                            lyDot.toVisible()
                            tvOnline.apply {
                                text = "Online"
                                setTextColor(ContextCompat.getColor(requireContext(),R.color.green_online))
                            }
                        }
                    }else{
                        binding.apply {
                            lyDot.toGone()
                            tvOnline.apply {
                                text = "Offline"
                                setTextColor(ContextCompat.getColor(requireContext(),R.color.gray_offline))
                            }
                        }
                    }
                }
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getListMessage()
        handleEventElements()
        super.onViewCreated(view, savedInstanceState)
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
        val newVoice = MessageAudio("111","111",MessageType.AUDIO,false,MediaHelper.mFileName,false,0)
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
            val message = MessageText(UUID.randomUUID().toString(),prefs.readIdUserLogin()!!,MessageType.TEXT, message = content).convertToMessageUpload()
            val randomKey = database.reference.push().key
            val lastMsgObj = hashMapOf<String,Any>()
            lastMsgObj["lastMsg"] = message.message
            lastMsgObj["lastMsgTime"] = message.time

            database.reference.child("chats").child(chatsMessageAdapter.senderRoom).updateChildren(lastMsgObj)
            database.reference.child("chats").child(chatsMessageAdapter.reciverRoom).updateChildren(lastMsgObj)

            if (randomKey != null) {
                database.reference.child("chats").child(chatsMessageAdapter.senderRoom).child("conversation").child(randomKey).setValue(message).addOnSuccessListener {
                    database.reference.child("chats").child(chatsMessageAdapter.reciverRoom).child("conversation").child(randomKey).setValue(message).addOnSuccessListener {
                        binding.edtSendMessage.apply {
                            hideKeyboard()
                            clearFocus()
                            setText("")
                        }
                    }.addOnFailureListener {
                        binding.root.showSnackbar("Đối phương chưa nhận được tin nhắn đâu :(")
                    }.addOnCanceledListener {
                        binding.root.showSnackbar("Có lỗi xảy ra rồi :(")
                    }
                }.addOnFailureListener {
                    binding.root.showSnackbar("Có thể lỗi mạng đó :(")
                }.addOnCanceledListener {
                    binding.root.showSnackbar("Gửi đi chưa thành công :(")
                }
            }
        }
        else{
            binding.root.showSnackbar("Bạn nên gửi lời yêu thương đi ~")
        }
    }

    private fun initRecyclerView() {
        chatsMessageAdapter = ChatsMessageAdapter(this@FragmentInbox,prefs.readIdUserLogin(),args.data?.id,
            args.data?.images?.get(0).toString())
        binding.recyclerViewChat.apply {
            listMessage = mutableListOf()
            chatsMessageAdapter.submitList(listMessage.toList())
            adapter = ScaleInAnimationAdapter(chatsMessageAdapter).apply {
                setDuration(1000)
                setInterpolator(OvershootInterpolator())
                setFirstOnly(true)
            }
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false).apply {
                    stackFromEnd = true
                }
            scrollToPosition(chatsMessageAdapter.itemCount+1)
        }
        chatsMessageAdapter.setOnItemImageClickListener { view, url ->
            requireActivity().sendImageToFullScreenImageActivity(view)
        }
        Log.d(TAG, "initRecyclerView: ${chatsMessageAdapter.differ.currentList.size}")
    }

    private fun getListMessage() {
        database.reference.child("chats").child(chatsMessageAdapter.senderRoom).child("conversation")
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    listMessage.clear()
                    snapshot.children.forEach {
                        val message : MessageUpload? = it.getValue(MessageUpload::class.java)
                        if (message != null) {
                            listMessage.add(message.convertToMessageModel(message.type))
                        }
                    }
                    chatsMessageAdapter.submitList(listMessage.toList())
                    binding.recyclerViewChat.smoothScrollToPosition(chatsMessageAdapter.differ.currentList.size+1)
                }

                override fun onCancelled(error: DatabaseError) {
                    binding.root.showSnackbar("Kiếm tra lại kết nối mạng!! ${error.message}")
                }

            })

    }

    private fun handleOnBackPress() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                sharedViewModel.setPositionMainViewPager(1)
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if(requestCode == RC_MEDIA)  openBottomImagePicker() else startRecording()
    }

    private fun openBottomImagePicker() {
            val tedBottomPicker =
                TedBottomPicker.with(requireActivity())
                    .setOnMultiImageSelectedListener {
                        it.forEach { uri ->

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