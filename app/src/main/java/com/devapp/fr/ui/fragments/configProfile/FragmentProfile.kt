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
    private val authViewModel: AuthAndProfileViewModel by viewModels()
    private lateinit var profileImageAdapter: ProfileImagesAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()
    override fun onSetupView() {
        Log.d(TAG, "onSetupView")
        authViewModel.getUserProfile(args.id)
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
        subscribeObserver()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun subscribeObserver() {
        launchRepeatOnLifeCycleWhenStarted {

            authViewModel.stateGetUserProfile.collect {
                when (it) {
                    is ResourceRemote.Loading -> {
                        Log.d(TAG, "subscriberObserver: loading...")
                    }

                    is ResourceRemote.Success -> {
                        binding.lyLoading.toGone()
                        binding.lyProfile.toVisible()
                        updateSharedViewModel(it.data)
                        updateUiProfile(it.data)
                    }

                    is ResourceRemote.Error -> {
                        binding.lyLoading.toVisible()
                        Toast.makeText(requireContext(), "Có lỗi xảy ra ~", Toast.LENGTH_SHORT)
                            .show()
                    }
                    else -> {

                    }
                }

            }
        }
    }

    private fun updateSharedViewModel(data: UserProfile?) {
        data?.let {
            sharedViewModel.setSharedFlowBasicInformation(hashMapOf(0 to data.name,1 to data.dob,2 to data.address,3 to data.gender))
            sharedViewModel.setSharedFlowJob(data.job)
            sharedViewModel.setSharedFlowSexuality(data.purpose)
            sharedViewModel.setSharedFlowInterest(data.interests?: mutableListOf())
            sharedViewModel.setSharedFlowIntroduce(data.bio)
            
            data.additionInformation?.let {
                sharedViewModel.setListItemInformation(DataHelper.getListItemInformation())
                sharedViewModel.setPositionInformation(-1)
                sharedViewModel.setSharedFlowTall(it.tall+1001)
                sharedViewModel.setSharedFlowChild(it.child+1001)
                sharedViewModel.setSharedFlowDrink(it.drink+1001)
                sharedViewModel.setSharedFlowMaritalStatus(it.maritalStatus+1001)
                sharedViewModel.setSharedFlowChooseGender(it.trueGender+1001)
                sharedViewModel.setSharedFlowSmoke(it.smoking+1001)
                sharedViewModel.setSharedFlowPet(it.pet+1001)
                sharedViewModel.setSharedFlowReligion(it.religion+1001)
                sharedViewModel.setSharedFlowCertificate(it.certificate+1001)
            }
        }
    }

    private fun updateUiProfile(data: UserProfile?) {
        data?.let {
            binding.apply {
                launchRepeatOnLifeCycleWhenResumed {
                    sharedViewModel.getSharedFlowIntroduce()
                        .distinctUntilChanged()
                        .collectLatest {
                            tvDesProfile.text = it.ifEmpty { "..." }
                        }
                }
                launchRepeatOnLifeCycleWhenResumed {
                    sharedViewModel.getSharedFlowBasicInformation()
                        .distinctUntilChanged()
                        .collectLatest {
                            tvName.text = it[0] as String
                        }
                }
                data.images?.let {
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

    override fun onDestroyView() {
        authViewModel.resetStateGetUserProfile()
        super.onDestroyView()
    }

}
