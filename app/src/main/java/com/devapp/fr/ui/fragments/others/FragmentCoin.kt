package com.devapp.fr.ui.fragments.others

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devapp.fr.R
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.entities.UserProfile
import com.devapp.fr.databinding.FragmentCoinBinding
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.activities.MainActivity
import com.devapp.fr.ui.viewmodels.AuthAndProfileViewModel
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.util.UiHelper.showSnackbar
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.extensions.*
import com.devapp.fr.util.storages.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import kotlin.math.floor

@AndroidEntryPoint
class FragmentCoin : BaseFragment<FragmentCoinBinding>(),Animation.AnimationListener{
    private val coins = intArrayOf(200,1000,700,400,200,1000,1200,100,1200,3000,600,1200)
    private var durationSpinner:Long = 2000L
    private var revolutionRotate = 0f
    private var isSpinning = false
    private var coinsResult = 0
    private var currentCoins = 0
    private var count = 0
    private var userProfile:UserProfile?=null
    private val args:FragmentCoinArgs by navArgs()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val authAndProfileViewModel: AuthAndProfileViewModel by activityViewModels()
    @Inject
    lateinit var pref: SharedPreferencesHelper
    @SuppressLint("ClickableViewAccessibility")

    override fun onSetupView() {
        handleOnBackPress()
        binding.includeSpinner.powerButton.setOnTouchListener { v, event ->
            when(event!!.action){
                MotionEvent.ACTION_DOWN->{
                    v.scaleDown()
                    if(isSpinning){
                        showToast("Đợi một chút nhé...")
                    }
                    else {
                        isSpinning = false
                    }
                    true
                }
                MotionEvent.ACTION_UP->{
                    if(!isSpinning){
                        startRotateSpinner()
                    }
                    v.scaleUp()
                    false
                }
                else -> {
                    false
                }
            }
        }
        binding.boxAnimation.setOnClickWithAnimationListener {
            (requireActivity() as MainActivity).onBackPressed()
        }
    }

    private fun handleOnBackPress() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                sharedViewModel.setPositionMainViewPager(0)
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    private fun startRotateSpinner(){
        durationSpinner = (floor(Math.random()*2000*count) +1000).toLong()
        revolutionRotate = (floor(Math.random()*6+1)*360).toInt().toFloat()
        val numOfCoins = coins.size
        val degreesPerSize = 360/numOfCoins
        val endDegrees = floor(Math.random()*360).toInt()
        val rotateAnimation = RotateAnimation(0f,
            (revolutionRotate+endDegrees),Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f).apply {
                interpolator = DecelerateInterpolator()
                repeatCount = 0
                duration = durationSpinner
                fillAfter = true
                setAnimationListener(this@FragmentCoin)
        }
        coinsResult = coins[endDegrees/degreesPerSize]
        binding.includeSpinner.imageWheel.startAnimation(rotateAnimation)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userProfile = (requireActivity() as MainActivity).getUser()
        launchRepeatOnLifeCycleWhenCreated {
            sharedViewModel.getSharedFlowCoins()
                .distinctUntilChanged()
                .collect {
                    binding.tvCoins.text ="$it coins"
                    currentCoins = it
                }
        }
        subscribeObserver()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAnimationStart(animation: Animation?) {
        isSpinning = true
    }

    override fun onAnimationEnd(animation: Animation?) {
        authAndProfileViewModel.updateByFiledName(args.id,"coins",coinsResult+currentCoins)
        isSpinning = false
    }

    override fun onAnimationRepeat(animation: Animation?) {

    }

    private fun subscribeObserver() {
        launchRepeatOnLifeCycleWhenStarted {
            authAndProfileViewModel.stateFieldName.collect {
                when (it) {
                    is ResourceRemote.Loading -> {
                        loadingDialog.show()
                    }

                    is ResourceRemote.Success -> {
                        loadingDialog.dismiss()
                        count+=1
                        sharedViewModel.setSharedFlowCoins(currentCoins+coinsResult)
                        if(coinsResult!=0)
                        binding.root.showSnackbar("Bạn nhận được $coinsResult coins")
                    }

                    is ResourceRemote.Error -> {
                        showToast("Có lỗi xảy ra ~")
                    }
                    else -> {

                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        authAndProfileViewModel.resetStateFieldName()
        super.onDestroyView()
    }
}