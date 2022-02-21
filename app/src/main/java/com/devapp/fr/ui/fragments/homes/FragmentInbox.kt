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
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.devapp.fr.R
import com.devapp.fr.adapters.ChatsMessageAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.models.MessageType
import com.devapp.fr.data.models.messages.MessageAudio
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
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenResumed
import com.devapp.fr.util.storages.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedbottompicker.TedBottomPicker
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
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
                it?.let {
                    if (it) {
                        Log.d(TAG, "subscriberObserver: send message successfully ~")
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
                    chatsMessageAdapter.submitList(it)
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
            realTimeViewModel.stateReadActing.collect {
                it?.let {
                    if (it.isEmpty()) binding.loading.toGone() else {
                        binding.loading.toVisible()
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
                    it.GONE()
                    VISIBLE()
                    start()
                }
                if (PermissionHelper.hasPermissionAudio(requireContext())) startRecording()
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
        MediaHelper.startRecording(requireContext())
    }

    private fun stopRecording() {
        MediaHelper.stopRecording(requireContext())
        val newVoice =
            MessageAudio("111", "111", MessageType.AUDIO, false, MediaHelper.mFileName, false, 0)
        val list = chatsMessageAdapter.differ.currentList.toMutableList()
        list.add(newVoice)
        chatsMessageAdapter.submitList(list)
        binding.recyclerViewChat.smoothScrollToPosition(binding.recyclerViewChat.adapter!!.itemCount + 1)
    }

    private fun handleSendMessage() {
        val content = binding.edtSendMessage.text.toString()
        if (content.isNotEmpty()) {
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
        } else {
            binding.root.showSnackbar("Bạn nên gửi lời yêu thương đi ~")
        }
    }

    private fun initRecyclerView() {
        chatsMessageAdapter = ChatsMessageAdapter(
            this@FragmentInbox, prefs.readIdUserLogin(), args.data?.id,
            args.data?.images?.get(0).toString()
        )
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

    private fun callClientApi() {
        args.data?.let { realTimeViewModel.checkUserOnOffbyId(it.id) }
        realTimeViewModel.getListMessage(chatsMessageAdapter.senderRoom)
        realTimeViewModel.readActing(chatsMessageAdapter.senderRoom)
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
}