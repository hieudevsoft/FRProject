package com.devapp.fr.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.devapp.fr.adapters.ProfileImagesAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.entities.UserProfile
import com.devapp.fr.databinding.FragmentProfileBinding
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.viewmodels.AuthViewModel
import com.devapp.fr.util.GlideApp
import com.devapp.fr.util.UiHelper.sendImageToFullScreenImageActivity
import com.devapp.fr.util.UiHelper.toGone
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FragmentProfile : BaseFragment<FragmentProfileBinding>() {
    val TAG = "FragmentProfile"
    private val args: FragmentProfileArgs by navArgs()
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var profileImageAdapter:ProfileImagesAdapter
    override fun onSetupView() {
        authViewModel.getUserProfile(args.id)
        setupProfileImageAdapter()
    }

    private fun setupProfileImageAdapter() {
        profileImageAdapter = ProfileImagesAdapter(this).apply {
            setOnItemClickListener { view, s ->
                requireActivity().sendImageToFullScreenImageActivity(view,s) }
        }
        binding.rcImage.apply {
            adapter = profileImageAdapter
            layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        handleEvent()
        subscribeObserver()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun subscribeObserver() {
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.stateGetUserProfile.collect {
                    when (it) {
                        is ResourceRemote.Loading -> {
                            Log.d(TAG, "subscriberObserver: loading...")
                        }

                        is ResourceRemote.Success -> {
                            binding.lyLoading.toGone()
                            binding.lyProfile.toVisible()
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

    }

    private fun updateUiProfile(data: UserProfile?) {
        data?.let {
            binding.apply {
                tvName.text = data.name
                tvDesProfile.text = data.bio
                data.images?.let {
                    GlideApp.loadImage(it[0],icAvatar,this@FragmentProfile)
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
        }
    }

    override fun onDestroyView() {
        authViewModel.resetStateGetUserProfile()
        super.onDestroyView()
    }

}
