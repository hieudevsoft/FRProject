package com.devapp.fr.ui.fragments.homes

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.devapp.fr.R
import com.devapp.fr.adapters.CardStackViewAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentLovesBinding
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.activities.MainActivity
import com.devapp.fr.ui.fragments.FragmentMainViewPager
import com.devapp.fr.ui.viewmodels.AuthAndProfileViewModel
import com.devapp.fr.ui.widgets.CustomDialog
import com.devapp.fr.util.Constants.LIMIT_REQUEST_SWIPE
import com.devapp.fr.util.UiHelper.toGone
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenStarted
import com.devapp.fr.util.storages.SharedPreferencesHelper
import com.yuyakaido.android.cardstackview.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class FragmentLoves : BaseFragment<FragmentLovesBinding>(), CardStackListener {
    val TAG = "FragmentLoves"
    val DEFAULT_VISIBLE_COUNT = 3
    val DEFAULT_TRANSLATION_INTERVAL = 8.0
    val DEFAULT_MAX_DEGREE = 36
    val DEFAULT_LIST_DIRECTION = Direction.HORIZONTAL
    val DEFAULT_SWIPE_THRESH_HOLD = 0.3

    private lateinit var cardStackViewAdapter: CardStackViewAdapter
    private lateinit var cardStackLayoutManager: CardStackLayoutManager
    private lateinit var settingSwipe: SwipeAnimationSetting.Builder
    private lateinit var settingRewind: RewindAnimationSetting.Builder
    private lateinit var parent: FragmentMainViewPager

    @Inject
    lateinit var prefs: SharedPreferencesHelper
    private val authAndProfileViewModel: AuthAndProfileViewModel by activityViewModels()
    private lateinit var loadingDialog: CustomDialog

    override fun onSetupView() {
        parent = (parentFragment as FragmentMainViewPager)
        loadingDialog = CustomDialog(R.layout.dialog_loading)
        subscribeObserver()
        binding.apply {
            setupCardStackViewLayoutManger()
            setupCardStackView()
        }
    }

    private fun setupLinearProgress(progress: Int) {
        binding.progressBarEnergy.setProgress(progress, true)
    }

    private fun setupCardStackView() {
        cardStackViewAdapter = CardStackViewAdapter(
            this,
            {
                swipeWithDirection(Direction.Left)
            }, {
                swipeWithDirection(Direction.Right)
            }, { view, user ->

            }) {
            parent.binding.mainViewPager.isUserInputEnabled = false
        }
        try {
            val user = (requireActivity() as MainActivity).getUser()
            user?.let {
                authAndProfileViewModel.getAllProfileSwipe(user.id, user.gender, LIMIT_REQUEST_SWIPE)
            }
        } catch (e: Exception) {

        }

        binding.cardStackView.apply {
            layoutManager = cardStackLayoutManager
            adapter = cardStackViewAdapter
        }
    }

    fun swipeWithDirection(direction: Direction) {
        cardStackLayoutManager.setSwipeAnimationSetting(
            settingSwipe.setDirection(direction).build()
        )
        binding.cardStackView.swipe()
    }

    private fun setupCardStackViewLayoutManger() {
        cardStackLayoutManager = CardStackLayoutManager(requireContext(), this)
        settingSwipe = SwipeAnimationSetting.Builder()
        settingRewind = RewindAnimationSetting.Builder()
        settingSwipe.apply {
            setDuration(Duration.Slow.duration)
            setInterpolator(AccelerateInterpolator())
        }
        settingRewind.apply {
            setDuration(Duration.Normal.duration)
            setInterpolator(DecelerateInterpolator())
        }
        cardStackLayoutManager.apply {
            setSwipeAnimationSetting(settingSwipe.build())
            setRewindAnimationSetting(settingRewind.build())
            setOverlayInterpolator(LinearInterpolator())
            setStackFrom(StackFrom.Top)
            setVisibleCount(DEFAULT_VISIBLE_COUNT)
            setTranslationInterval(DEFAULT_TRANSLATION_INTERVAL.toFloat())
            setMaxDegree(DEFAULT_MAX_DEGREE.toFloat())
            setDirections(DEFAULT_LIST_DIRECTION)
            setSwipeThreshold(DEFAULT_SWIPE_THRESH_HOLD.toFloat())
            setCanScrollHorizontal(true)
            setCanScrollVertical(false)
            setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
        if (direction?.name!!.contains("left", true)) {
            binding.cardLove.toGone()
            binding.cardCancel.toVisible()
            binding.cardCancel.alpha = if (ratio < 0.2) 0f else ratio
        } else {
            binding.cardCancel.toGone()
            binding.cardLove.toVisible()
            binding.cardLove.alpha = if (ratio < 0.2) 0f else ratio
        }
        Log.d(TAG, "onCardDragging: $ratio")
    }

    override fun onCardSwiped(direction: Direction?) {
        goneCardOperation()
        Log.d(TAG, "onCardSwiped: ${direction?.name}")
        if (direction?.name!!.contains("RIGHT", true))
            setupLinearProgress(binding.progressBarEnergy.progress + 1)
        else {
            try {
                setupLinearProgress(binding.progressBarEnergy.progress - 1)
            } catch (e: Exception) {

            }
        }
    }

    private fun goneCardOperation() {
        binding.apply {
            cardLove.toGone()
            cardCancel.toGone()
        }
    }

    override fun onCardRewound() {
        Log.d(TAG, "onCardRewound: Card Rewound")
        goneCardOperation()
        //setupLinearProgress(binding.progressBarEnergy.progress - 1)
    }

    override fun onCardCanceled() {
        goneCardOperation()
        parent.binding.mainViewPager.isUserInputEnabled = true
        Log.d(TAG, "onCardCanceled: Card Cancel")
    }

    override fun onCardAppeared(view: View?, position: Int) {
        goneCardOperation()
        Log.d(TAG, "onCardAppeared: $view $position")
        if (position == 0) binding.imgHeart.setImageResource(R.drawable.ic_heart)
        else binding.imgHeart.setImageResource(R.drawable.ic_rewind)
        binding.imgHeart.setOnClickListener {
            if (position != 0) binding.cardStackView.rewind()
        }
    }

    override fun onCardDisappeared(view: View?, position: Int) {
        Log.d(TAG, "onCardDisappeared: $view $position")
        goneCardOperation()
    }

    private fun subscribeObserver() {
        launchRepeatOnLifeCycleWhenStarted {

            authAndProfileViewModel.sateGetAllProfileSwipe.collect {
                when (it) {
                    is ResourceRemote.Loading -> {
                        loadingDialog.show(childFragmentManager, loadingDialog.tag)
                        Log.d(TAG, "subscriberObserver: loading...")
                    }

                    is ResourceRemote.Success -> {
                        loadingDialog.dismiss()
                        binding.cardStackView.toVisible()
                        cardStackViewAdapter.submitList(it.data)
                        binding.progressBarEnergy.max = cardStackViewAdapter.itemCount
                        binding.progressBarEnergy.min = 0
                        Log.d(TAG, "setupCardStackView: ${cardStackViewAdapter.itemCount}")
                    }

                    is ResourceRemote.Error -> {
                        loadingDialog.dismiss()
                        binding.cardStackView.toGone()
                        Log.d(TAG, "subscribeObserver: ${it.message}")
                        Toast.makeText(
                            requireContext(),
                            "Kiểm tra lại kết nối mạng!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {

                    }
                }

            }
        }
    }

    override fun onDestroyView() {
        authAndProfileViewModel.resetSateGetAllProfileSwipe()
        super.onDestroyView()
    }

}