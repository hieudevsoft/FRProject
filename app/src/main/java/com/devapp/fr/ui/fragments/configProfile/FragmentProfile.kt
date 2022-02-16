package com.devapp.fr.ui.fragments.configProfile

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.devapp.fr.R
import com.devapp.fr.adapters.ProfileImagesAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.entities.UserProfile
import com.devapp.fr.databinding.FragmentProfileBinding
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.viewmodels.AuthAndProfileViewModel
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.util.DataHelper
import com.devapp.fr.util.GlideApp
import com.devapp.fr.util.UiHelper.sendImageToFullScreenImageActivity
import com.devapp.fr.util.UiHelper.toGone
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenResumed
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@AndroidEntryPoint
class FragmentProfile : BaseFragment<FragmentProfileBinding>() {
    val TAG = "FragmentProfile"
    private val args: FragmentProfileArgs by navArgs()
    private lateinit var profileImageAdapter: ProfileImagesAdapter
    private val sharedViewModel:SharedViewModel by activityViewModels()
    override fun onSetupView() {
        Log.d(TAG, "onSetupView")
        setupProfileImageAdapter()
    }

    private fun setupProfileImageAdapter() {
        profileImageAdapter = ProfileImagesAdapter(this).apply {
            setOnItemClickListener { view, s ->
                requireActivity().sendImageToFullScreenImageActivity(view, s)
            }
        }
        binding.rcImage.apply {
            adapter = profileImageAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        handleEvent()
        updateUiProfile()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun updateUiProfile() {
            binding.apply {
                launchRepeatOnLifeCycleWhenResumed {
                    sharedViewModel.getSharedFlowIntroduce()
                        .distinctUntilChanged()
                        .collectLatest {
                            Log.d(TAG, "updateUiProfile: $it")
                            tvDesProfile.text = it.ifEmpty { "..." }
                        }
                }
                launchRepeatOnLifeCycleWhenResumed {
                    sharedViewModel.getSharedFlowBasicInformation()
                        .distinctUntilChanged()
                        .collectLatest {
                            binding.apply {
                                lyLoading.toGone()
                                lyProfile.toVisible()
                            }
                            tvName.text = it[0] as String
                        }
                }
                launchRepeatOnLifeCycleWhenResumed {
                    sharedViewModel.getSharedFlowImage()
                        .distinctUntilChanged()
                        .collectLatest {
                            GlideApp.loadImage(it[0], icAvatar, this@FragmentProfile)
                            profileImageAdapter.submitList(it)
                        }
                }
            }

    }

    private fun handleEvent() {
        binding.apply {
            btnBack.setOnClickWithAnimationListener {
                findNavController().popBackStack()
            }
            lyEditProfile.setOnClickWithAnimationListener {
                findNavController().navigate(
                    FragmentProfileDirections.actionFragmentProfileToFragmentEditProfile(
                        args.id
                    )
                )
            }
        }
    }


}
