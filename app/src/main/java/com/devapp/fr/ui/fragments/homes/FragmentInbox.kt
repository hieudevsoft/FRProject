package com.devapp.fr.ui.fragments.homes

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator
import android.view.inputmethod.EditorInfo.IME_ACTION_SEND
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.devapp.fr.R
import com.devapp.fr.adapters.ChatsMessageAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.entities.googles.FingerPath
import com.devapp.fr.data.entities.googles.PostBodyFinger
import com.devapp.fr.data.entities.googles.Request
import com.devapp.fr.data.entities.googles.WritingGuide
import com.devapp.fr.data.models.MessageType
import com.devapp.fr.data.models.interfaces.HandFingerCallback
import com.devapp.fr.data.models.messages.MessageAudio
import com.devapp.fr.data.models.messages.MessageImage
import com.devapp.fr.data.models.messages.MessageText
import com.devapp.fr.databinding.FragmentInboxBinding
import com.devapp.fr.ui.activities.VideoCallActivity
import com.devapp.fr.ui.viewmodels.HandWriterViewModel
import com.devapp.fr.ui.viewmodels.RealTimeViewModel
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.ui.viewmodels.StorageViewModel
import com.devapp.fr.ui.widgets.FingerBottomDialogFragment
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
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.extensions.*
import com.devapp.fr.util.storages.SharedPreferencesHelper
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedbottompicker.TedBottomPicker
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class FragmentInbox : BaseFragment<FragmentInboxBinding>(), EasyPermissions.PermissionCallbacks,
    HandFingerCallback {
    val TAG = "FragmentInbox"
    private lateinit var chatsMessageAdapter: ChatsMessageAdapter
    private lateinit var speechRecognizerIntent: Intent
    private lateinit var speechRecognizer: SpeechRecognizer
    private val args: FragmentInboxArgs by navArgs()
    private var currentPositionReact = -1
    private var nameOwner = ""
    private var roomId = ""

    @Inject
    lateinit var prefs: SharedPreferencesHelper
    private val realTimeViewModel: RealTimeViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val storageViewModel: StorageViewModel by activityViewModels()
    private val handWriterViewModel: HandWriterViewModel by activityViewModels()

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
        ) { pos, item ->
            realTimeViewModel.updateMessage(
                chatsMessageAdapter.senderRoom,
                chatsMessageAdapter.reciverRoom,
                item
            )
            currentPositionReact = pos
        }
        binding.recyclerViewChat.apply {
            adapter = ScaleInAnimationAdapter(chatsMessageAdapter).apply {
                setDuration(1000)
                setInterpolator(OvershootInterpolator())
                setFirstOnly(true)
            }
            isNestedScrollingEnabled = false
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
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
                    if (it.isNotEmpty()) {
                        val listSubmit = it.map {
                            it.isMe = it.userId == prefs.readIdUserLogin()!!
                            it
                        }
                        val mapPushCompareSeen = hashMapOf<String, Any>(
                            "new_size" to listSubmit.size,
                            "old_size" to listSubmit.size
                        )
                        realTimeViewModel.updateSizeCompareSeenSender(
                            chatsMessageAdapter.senderRoom,
                            mapPushCompareSeen
                        )
                        realTimeViewModel.updateSizeCompareSeenReciever(
                            chatsMessageAdapter.reciverRoom,
                            listSubmit.size
                        )
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
                    withContext(Dispatchers.IO) {
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
                    withContext(Dispatchers.IO) {
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

        launchRepeatOnLifeCycleWhenCreated {
            sharedViewModel.getSharedFlowBasicInformation().distinctUntilChanged()
                .collect {
                    nameOwner = it[0] as String
                }
        }

        launchRepeatOnLifeCycleWhenCreated {
            realTimeViewModel.stateFlowNotificationCallVideo.collect {
                it?.let {
                    if (it) {
                        realTimeViewModel.resetStateFlowNotificationCallVideo()
                    } else {
                        binding.root.showSnackbar("Có lỗi xảy ra :(")
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        callClientApi()
        handleEventElements()
        setupSpeechRecognizer()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(
            requireContext(),
            ComponentName.unflattenFromString("com.google.android.googlequicksearchbox/com.google.android.voicesearch.serviceapi.GoogleRecognitionService")
        )
        speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi")
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Log.d(TAG, "onReadyForSpeech: Ready~")
            }

            override fun onBeginningOfSpeech() {
                Log.d(TAG, "onBeginningOfSpeech: Listening...")
            }

            override fun onRmsChanged(rmsdB: Float) {
                Log.d(TAG, "onRmsChanged: RmsChanged...")
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                Log.d(TAG, "onBufferReceived: BufferReceived...")
            }

            override fun onEndOfSpeech() {
                Log.d(TAG, "onEndOfSpeech: End Of Speech...")
            }

            override fun onError(error: Int) {
                Snackbar.make(binding.root, "Xảy ra lỗi!!", Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.RED)
                    .show()
                Log.d(TAG, "onError: $error")
            }

            override fun onResults(results: Bundle?) {
                binding.imgSpeech.setImageResource(R.drawable.ic_speak_red)
                val result = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                result?.let {
                    binding.edtSendMessage.setText(result[0].toString().trim())
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                Log.d(TAG, "onPartialResults: onPartialResults")
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                Log.d(TAG, "onEvent: onEvent... $eventType")
            }

        })
    }

    override fun onDestroy() {
        try {
            speechRecognizer.destroy()
        } catch (e: Exception) {
        }
        super.onDestroy()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handleEventElements() {
        binding.apply {
            ibVoiceTemp.setOnClickListener {
                pulIbVoice.apply {
                    if (PermissionHelper.hasPermissionAudio(requireContext())) {
                        it.GONE()
                        VISIBLE()
                        start()
                        startRecording()
                    } else PermissionHelper.requestPermissionAudio(this@FragmentInbox)
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
                if (it != null)
                    realTimeViewModel.updateActing(
                        chatsMessageAdapter.reciverRoom,
                        if (it.isEmpty()) "" else "đang nhập ..."
                    )
            }

            ibCamera.setOnClickListener {
                if (PermissionHelper.hasPermissionBottomPicker(requireContext())) openBottomImagePicker() else
                    PermissionHelper.requestPermissionBottomPicker(this@FragmentInbox)
            }

            cardRegSpeak.setOnTouchListener { v, event ->
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    if (PermissionHelper.hasPermissionAudio(requireContext())) {
                        v.scaleDown()
                        binding.imgSpeech.setImageResource(R.drawable.ic_speak_blue)
                        speechRecognizer.startListening(speechRecognizerIntent)
                    } else PermissionHelper.requestPermissionAudio(this@FragmentInbox)
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    if (PermissionHelper.hasPermissionAudio(requireContext())) {
                        binding.imgSpeech.setImageResource(R.drawable.ic_speak_red)
                        speechRecognizer.stopListening()
                        v.scaleUp()
                    } else showToast("Thông qua quyền để thực hiện!")
                }
                true
            }

            ibHandWrite.setOnClickWithAnimationListener {
                openBottomHandWriter()
            }

            ibInformationInbox.setOnClickWithAnimationListener {
                navigateFragmentInformationInbox()
            }

            ibCallVideo.setOnClickWithAnimationListener {
                roomId = UUID.randomUUID().toString()
                val roomIdJoin = roomId
                launchRepeatOnLifeCycleWhenResumed {
                    realTimeViewModel.readNotificationCallVideo(args.data!!.id) {
                        if (it == "##" && roomId.isEmpty()) {
                            showToast("Bạn bị ${args.data!!.name} từ chối mất rồi :(")
                        } else if (it?.replace("##","")?.trim() == prefs.readIdUserLogin()!!) {
                            startActivity(
                                Intent(
                                    requireActivity(),
                                    VideoCallActivity::class.java
                                ).also {
                                    it.putExtra("roomId", roomIdJoin)
                                    it.putExtra("partnerId", args.data!!.id)
                                })
                        } else {
                            if (it == null || it.trim().contains("null") || (it == "##" && roomId.isNotEmpty()) || it.split("#")[0].isEmpty()) {
                                realTimeViewModel.sendNotificationCallVideo(
                                    args.data!!.id,
                                    roomId,
                                    nameOwner,
                                    prefs.readIdUserLogin()!!
                                )
                                roomId = ""
                            } else if (it.isNotEmpty() && it != "null##" && !it.contains(roomId)) {
                                binding.root.showSnackbar("Đối phương đã có cuộc gọi khác :(")
                            }
                        }
                    }
                }
            }
        }
    }

        private fun navigateFragmentInformationInbox() {

        }

        private fun openBottomHandWriter() {
            FingerBottomDialogFragment(this, handWriterViewModel) {
                binding.edtSendMessage.setText(
                    binding.edtSendMessage.text.toString().trim() + " " + it
                )
            }.show(childFragmentManager, "finger")
        }

        private fun startRecording() {
            Log.d(TAG, "startRecording")
            MediaHelper.startRecording(requireContext())
        }

        private fun stopRecording() {
            Log.d(TAG, "stopRecording")
            MediaHelper.stopRecording(requireContext())
            storageViewModel.addAudio(
                chatsMessageAdapter.senderRoom,
                chatsMessageAdapter.reciverRoom,
                Uri.fromFile(File(MediaHelper.mFileName))
            )
            binding.root.showSnackbar("Chờ một chút nhé ~")
        }

        private fun handleSendMessageText() {
            val content = binding.edtSendMessage.text.toString()
            if (content.isNotEmpty()) {
                lifecycleScope.launchWhenCreated {
                    withContext(Dispatchers.IO) {
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
            if (it != null && it.toString().isNotEmpty()) {
                binding.root.showSnackbar("Chờ một chút nhé ~")
                storageViewModel.addImageChat(
                    chatsMessageAdapter.senderRoom,
                    chatsMessageAdapter.reciverRoom,
                    it
                )
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
            if (requestCode == RC_MEDIA) openBottomImagePicker() else {
                startRecording()
                setupSpeechRecognizer()
            }
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
            realTimeViewModel.updateActing(chatsMessageAdapter.reciverRoom, "")
            realTimeViewModel.resetStateSendMessageToFirebase()
            realTimeViewModel.resetStateGetListMessage()
            storageViewModel.resetStateAddImageChats()
            storageViewModel.resetStateAddAudio()
            super.onDestroyView()
        }

        override fun onDrawFinger(listFingerPath: List<FingerPath>) {
            val displayMetrics = DisplayMetrics()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requireActivity().display?.getRealMetrics(displayMetrics)
            } else requireActivity().windowManager.defaultDisplay.getRealMetrics(displayMetrics)
            val writingGuide = WritingGuide(displayMetrics.heightPixels, displayMetrics.widthPixels)
            val postBodyFinger = PostBodyFinger(
                "537.36",
                0.4,
                "5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36",
                0,
                "enable_pre_space",
                listOf(getRequest(listFingerPath, writingGuide))
            )
            handWriterViewModel.postBody(postBodyFinger)
        }

        private fun getRequest(
            listFingerPath: List<FingerPath>,
            writingGuide: WritingGuide
        ): Request {
            var inks = mutableListOf<List<List<Int>>>()
            listFingerPath.forEach {
                inks.add(listOf(it.getXPoints(), it.getYPoints(), it.time))
            }
            return Request(
                inks.toList(),
                0,
                10,
                "",
                writingGuide
            )
        }
    }