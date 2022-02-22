package com.devapp.fr.ui.fragments.others

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devapp.fr.R
import com.devapp.fr.adapters.ColorInBoxAdapter
import com.devapp.fr.adapters.ProfileImagesAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentEditInboxBinding
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.viewmodels.AuthAndProfileViewModel
import com.devapp.fr.ui.viewmodels.RealTimeViewModel
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.util.UiHelper
import com.devapp.fr.util.UiHelper.multilineIme
import com.devapp.fr.util.UiHelper.showSnackbar
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenResumed
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenStarted
import com.devapp.fr.util.extensions.showToast
import com.devapp.fr.util.storages.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@AndroidEntryPoint
class FragmentEditInbox:BaseFragment<FragmentEditInboxBinding>(){
    private val sharedViewModel:SharedViewModel by activityViewModels()
    private val authAndProfileViewModel:AuthAndProfileViewModel by activityViewModels()
    private val realTimeViewModel:RealTimeViewModel by activityViewModels()
    private lateinit var imagesAdapter:ProfileImagesAdapter
    private lateinit var colorInboxAdapter:ColorInBoxAdapter
    private val args:FragmentEditInboxArgs by navArgs()
    @Inject
    lateinit var pref:SharedPreferencesHelper
    private var currentCoins = 0
    val TAG = "FragmentEditInbox"
    override fun onSetupView() {
        binding.btnBack.setOnClickWithAnimationListener {
            findNavController().popBackStack()
        }
        setUpImageAdapter()
        setUpColorAdapter()
        setUpNickName()
    }

    private fun setUpColorAdapter() {
        colorInboxAdapter = ColorInBoxAdapter()
        val recyclerView = binding.cardColor.findViewById<RecyclerView>(R.id.innerView)
        recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(),5)
            adapter = colorInboxAdapter
            itemAnimator = OvershootInLeftAnimator()
        }
        colorInboxAdapter.submitList(resources.getStringArray(R.array.color_inbox).toList())
        colorInboxAdapter.setOnItemClickListener {
            UiHelper.triggerBottomAlertDialog(
                requireActivity(),
                "Hỏi ?",
                "Sẽ mất 2000 coins để thực hiện?",
                "Có",
                "Không",
                false,
                { dialogInterface, _ ->
                    currentCoins-=2000
                    if(currentCoins<0){
                        binding.root.showSnackbar("Kiếm thêm coins nhé ~")
                    } else{
                        realTimeViewModel.setColorBoxChat(it,args.senderRoom,args.receiverRoom)
                        authAndProfileViewModel.updateByFiledName(pref.readIdUserLogin()!!,"coins",currentCoins)
                    }
                    dialogInterface.dismiss()
                }
            )

        }
    }

    private fun setUpNickName() {
        val edtNickName = binding.cardNickName.findViewById<AppCompatEditText>(R.id.innerView)
        realTimeViewModel.getNickNamePartner(args.senderRoom){
            it?.let {
                edtNickName.setText(it)
            }
        }
        edtNickName.multilineIme(EditorInfo.IME_ACTION_SEND) {
            UiHelper.triggerBottomAlertDialog(
                requireActivity(),
                "Hỏi ?",
                "Sẽ mất 1000 coins để thực hiện?",
                "Có",
                "Không",
                false,
                { dialogInterface, _ ->
                    currentCoins-=1000
                    if(currentCoins<0){
                        binding.root.showSnackbar("Kiếm thêm coins nhé ~")
                    } else{
                        realTimeViewModel.setNickNameForPartner(edtNickName.text.toString().trim(),args.senderRoom)
                        authAndProfileViewModel.updateByFiledName(pref.readIdUserLogin()!!,"coins",currentCoins)
                    }
                    dialogInterface.dismiss()
                }
            )
        }
    }

    private fun setUpImageAdapter() {
        imagesAdapter = ProfileImagesAdapter(this)
        val recyclerView = binding.cardImages.findViewById<RecyclerView>(R.id.innerView)
        recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(),3)
            adapter = imagesAdapter
            itemAnimator = OvershootInLeftAnimator()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        subscriberFlow()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun subscriberFlow(){
        launchRepeatOnLifeCycleWhenResumed {
            sharedViewModel.getSharedFlowListImageInbox().distinctUntilChanged()
                .collectLatest {
                    imagesAdapter.submitList(it.filter { it.isNotEmpty() })
                }
        }

        launchRepeatOnLifeCycleWhenResumed {
            sharedViewModel.getSharedFlowCoins().distinctUntilChanged()
                .collectLatest {
                    currentCoins = it
                }
        }

        launchRepeatOnLifeCycleWhenStarted {
            authAndProfileViewModel.stateFieldName.collect {
                when (it) {
                    is ResourceRemote.Loading -> {
                        loadingDialog.show()
                    }

                    is ResourceRemote.Success -> {
                        loadingDialog.dismiss()
                        sharedViewModel.setSharedFlowCoins(currentCoins)
                    }

                    is ResourceRemote.Error -> {
                        showToast("Có lỗi xảy ra ~")
                    }
                    else -> {

                    }
                }
            }
        }

        launchRepeatOnLifeCycleWhenResumed {
            realTimeViewModel.stateFlowSetNickNameForPartner.collectLatest {
                it?.let {
                    if(it) binding.root.showSnackbar("Bạn đã đổi thành công ~")
                }
            }
        }

        launchRepeatOnLifeCycleWhenResumed {
            realTimeViewModel.stateFlowSetColorBoxChat.collectLatest {
                it?.let {
                    if(it) binding.root.showSnackbar("Bạn đã đổi màu thành công ~")
                }
            }
        }
    }

    override fun onDestroyView() {
        realTimeViewModel.resetStateSetNickNameForPartner()
        realTimeViewModel.resetStateSetColorBoxChat()
        super.onDestroyView()
    }

}