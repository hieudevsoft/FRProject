package com.devapp.fr.ui.fragments.homes

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator
import android.view.inputmethod.EditorInfo.IME_ACTION_SEND
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
import com.devapp.fr.ui.viewmodels.RealTimeViewModel
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.ui.viewmodels.StorageViewModel
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
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenResumed
import com.devapp.fr.util.storages.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedbottompicker.TedBottomPicker
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class FragmentInbox : BaseFragment<FragmentInboxBinding>(), EasyPermissions.PermissionCallbacks {
    val TAG = "FragmentInbox"
    private lateinit var chatsMessageAdapter: ChatsMessageAdapter
    private val args: FragmentInboxArgs by navArgs()

    @Inject
    lateinit var prefs: SharedPreferencesHelper
    private val realTimeViewModel: RealTimeViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val storageViewModel: StorageViewModel by activityViewModels()
    private var currentPositionReact = -1
    @SuppressLint("ResourceAsColor")
    override fun onSetupView() {
        initRecyclerView()
        handleOnBackPress()
        args.data?.let {
            it.images?.get(0)?.let { GlideApp.loadImage(it, binding.imgPartner, this) }
            val anim: Animation = AlphaAnimation(0.0f, 1.0f)
            anim.duration = 500
            anim.startOffset = 20
            anim.repeatMode = Animation.REVERSE
            anim.repeatCount = Animation.INFINITE
            binding.apply {
                dotOnline.startAnimation(anim)
                tvName.text = it.name
            }
            subscriberObserver()
        }
    }

    private fun initRecyclerView() {
        chatsMessageAdapter = ChatsMessageAdapter(
            this@FragmentInbox, prefs.readIdUserLogin(), args.data?.id,
            args.data?.images?.get(0).toString()
        ){
            pos,item->
            realTimeViewModel.updateMessage(chatsMessageAdapter.senderRoom,chatsMessageAdapter.reciverRoom,item)
            currentPositionReact = pos
            //chatsMessageAdapter.notifyItemChanged(currentPositionReact)
        }
        binding.recyclerViewChat.apply {
            adapter = ScaleInAnimationAdapter(chatsMessageAdapter).apply {
                setDuration(1000)
                setInterpolator(OvershootInterpolator())
                setFirstOnly(true)
            }
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        chatsMessageAdapter.setOnItemImageClickListener { view, url ->
            requireActivity().sendImageToFullScreenImageActivity(view)
        }
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

    private fun subscriberObserver() {
        launchRepeatOnLifeCycleWhenCreated { scope ->
            realTimeViewModel.stateFlowUserOnOff.collectLatest {
                if (it == true) {
                    binding.apply {
                        lyDot.toVisible()
                        tvOnline.apply {
                            text = "Online"
                            setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.green_online
                                )
                            )
                        }
                    }
                } else {
                    binding.apply {
                        lyDot.toGone()
                        tvOnline.apply {
                            text = "Offline"
                            setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.gray_offline
                                )
                            )
                        }
                    }
                }
            }
        }

        launchRepeatOnLifeCycleWhenCreated {
            realTimeViewModel.stateUpdateLastMessage.collectLatest {
                it?.let {
                    if (it) Log.d(TAG, "subscriberObserver: Update Last Message Successfully....")
                }
            }
        }

        launchRepeatOnLifeCycleWhenResumed {
            realTimeViewModel.stateSendMessageToFirebase
                .collect {
                    Log.d(TAG, "subscriberObserver: $it")
                it?.let {
                    if (it) {
                        Log.d(TAG, "subscriberObserver: send message successfully ~")
                        realTimeViewModel.resetStateSendMessageToFirebase()
                        binding.edtSendMessage.apply {
                            hideKeyboard()
                            clearFocus()
                            setText("")
                        }
                    } else {
                        binding.root.showSnackbar("Oops!!")
                    }
                }
            }
        }

        launchRepeatOnLifeCycleWhenResumed {
            realTimeViewModel.stateGetListMessage.collectLatest {
                if (it == null) {
                    binding.root.showSnackbar("Có gì đó không ổn! :(")
                } else {
                    if(it.isNotEmpty()){
                        val listSubmit = it.map {
                            it.isMe = it.userId == prefs.readIdUserLogin()!!
                            it
                        }
                        chatsMessageAdapter.submitList(listSubmit)
                    }

                }
            }
        }

        launchRepeatOnLifeCycleWhenCreated {
            realTimeViewModel.stateUpdateActing.collect {
                it?.let {
                    if (it) Log.d(TAG, "subscriberObserver: update acting...")
                }
            }
        }

        launchRepeatOnLifeCycleWhenCreated {
            realTimeViewModel.stateUpdateMessage.collect {
                it?.let {
                    if (it) {
                        Log.d(TAG, "subscriberObserver: update reaction...")
                        realTimeViewModel.resetStateFlowUpdateMessage()
                        chatsMessageAdapter.notifyItemChanged(currentPositionReact)
                    }
                }
            }
        }

        launchRepeatOnLifeCycleWhenCreated {
            realTimeViewModel.stateReadActing.collect {
                it?.let {
                    if (it.isEmpty()) binding.loading.toGone() else {
                        binding.loading.toVisible()
                    }
                }
            }
        }

        launchRepeatOnLifeCycleWhenCreated {
            storageViewModel.stateAddImageChats.collect {
                it?.let {
                    withContext(Dispatchers.IO){
                        val message = MessageImage(
                            "",
                            prefs.readIdUserLogin()!!,
                            MessageType.IMAGE,
                            urlImage = it.toString(),
                        ).convertToMessageUpload()
                        val lastMsgObj = hashMapOf<String, Any>()
                        lastMsgObj["lastMsg"] = "Gửi ảnh..."
                        lastMsgObj["lastMsgTime"] = message.time
                        realTimeViewModel.updateLastMessage(
                            chatsMessageAdapter.senderRoom,
                            chatsMessageAdapter.reciverRoom,
                            lastMsgObj
                        )
                        realTimeViewModel.sendMessageToFirebase(
                            chatsMessageAdapter.senderRoom,
                            chatsMessageAdapter.reciverRoom,
                            message
                        )
                    }
                }
            }
        }

        launchRepeatOnLifeCycleWhenCreated {
            storageViewModel.stateAddAudio.collect {
                Log.d(TAG, "subscriberObserver: $it")
                it?.let {
                    withContext(Dispatchers.IO){
                        val message = MessageAudio(
                            "",
                            prefs.readIdUserLogin()!!,
                            MessageType.AUDIO,
                            audio = it.toString(),
                        ).convertToMessageUpload()
                        val lastMsgObj = hashMapOf<String, Any>()
                        lastMsgObj["lastMsg"] = "Gửi âm thanh..."
                        lastMsgObj["lastMsgTime"] = message.time
                        realTimeViewModel.updateLastMessage(
                            chatsMessageAdapter.senderRoom,
                            chatsMessageAdapter.reciverRoom,
                            lastMsgObj
                        )
                        realTimeViewModel.sendMessageToFirebase(
                            chatsMessageAdapter.senderRoom,
                            chatsMessageAdapter.reciverRoom,
                            message
                        )
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        callClientApi()
        handleEventElements()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun handleEventElements() {
        binding.apply {
            ibVoiceTemp.setOnClickListener {
                pulIbVoice.apply {
                    if (PermissionHelper.hasPermissionAudio(requireContext())) {
                        it.GONE()
                        VISIBLE()
                        start()
                        startRecording()
                    }
                    else PermissionHelper.requestPermissionAudio(this@FragmentInbox)
                }

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
                handleSendMessageText()
            }

            cardSendMessage.setOnClickListener {
                handleSendMessageText()
            }

            edtSendMessage.addTextChangedListener {
                if(it!=null)
                realTimeViewModel.updateActing(chatsMessageAdapter.reciverRoom,if(it.isEmpty()) "" else "đang nhập ...")
            }

            ibCamera.setOnClickListener {
                if (PermissionHelper.hasPermissionBottomPicker(requireContext())) openBottomImagePicker() else
                    PermissionHelper.requestPermissionBottomPicker(this@FragmentInbox)
            }
        }
    }

    private fun startRecording() {
        Log.d(TAG, "startRecording")
        MediaHelper.startRecording(requireContext())
    }

    private fun stopRecording() {
        Log.d(TAG, "stopRecording")
        MediaHelper.stopRecording(requireContext())
        storageViewModel.addAudio(chatsMessageAdapter.senderRoom,chatsMessageAdapter.reciverRoom,Uri.fromFile(File(MediaHelper.mFileName)))
        binding.root.showSnackbar("Chờ một chút nhé ~")
    }

    private fun handleSendMessageText() {
        val content = binding.edtSendMessage.text.toString()
        if (content.isNotEmpty()) {
            lifecycleScope.launchWhenCreated {
                withContext(Dispatchers.IO){
                    val message = MessageText(
                        "",
                        prefs.readIdUserLogin()!!,
                        MessageType.TEXT,
                        message = content
                    ).convertToMessageUpload()
                    val lastMsgObj = hashMapOf<String, Any>()
                    lastMsgObj["lastMsg"] = message.message
                    lastMsgObj["lastMsgTime"] = message.time
                    realTimeViewModel.updateLastMessage(
                        chatsMessageAdapter.senderRoom,
                        chatsMessageAdapter.reciverRoom,
                        lastMsgObj
                    )
                    realTimeViewModel.sendMessageToFirebase(
                        chatsMessageAdapter.senderRoom,
                        chatsMessageAdapter.reciverRoom,
                        message
                    )
                }
            }
        } else {
            binding.root.showSnackbar("Bạn nên gửi lời yêu thương đi ~")
        }
    }
    private fun handleSendMessageImage(it: Uri?) {
        if (it!=null&&it.toString().isNotEmpty()) {
            binding.root.showSnackbar("Chờ một chút nhé ~")
            storageViewModel.addImageChat(chatsMessageAdapter.senderRoom,chatsMessageAdapter.reciverRoom,it)
        } else {
            binding.root.showSnackbar("Chưa chọn được ảnh ~")
        }
    }

    private fun callClientApi() {
        args.data?.let { realTimeViewModel.checkUserOnOffbyId(it.id) }
        realTimeViewModel.getListMessage(chatsMessageAdapter.senderRoom)
        realTimeViewModel.readActing(chatsMessageAdapter.senderRoom)
    }

    private fun openBottomImagePicker() {
        val tedBottomPicker =
            TedBottomPicker.with(requireActivity())
                .setOnImageSelectedListener {
                    handleSendMessageImage(it)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == RC_MEDIA) openBottomImagePicker() else startRecording()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            if (requestCode == RC_MEDIA)
                PermissionHelper.requestPermissionBottomPicker(this)
            else PermissionHelper.requestPermissionAudio(this)
        }
    }

    override fun onDestroyView() {
        realTimeViewModel.updateActing(chatsMessageAdapter.reciverRoom,"")
        realTimeViewModel.resetStateSendMessageToFirebase()
        storageViewModel.resetStateAddImageChats()
        storageViewModel.resetStateAddAudio()
        super.onDestroyView()
    }
}