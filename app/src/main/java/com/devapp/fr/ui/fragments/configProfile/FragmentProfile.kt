package com.devapp.fr.ui.fragments.configProfile

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.devapp.fr.R
import com.devapp.fr.adapters.ProfileImagesAdapter
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentProfileBinding
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.activities.MainActivity
import com.devapp.fr.ui.viewmodels.AuthAndProfileViewModel
import com.devapp.fr.ui.viewmodels.SharedViewModel
import com.devapp.fr.ui.viewmodels.StorageViewModel
import com.devapp.fr.util.GlideApp
import com.devapp.fr.util.PermissionHelper
import com.devapp.fr.util.UiHelper
import com.devapp.fr.util.UiHelper.sendImageToFullScreenImageActivity
import com.devapp.fr.util.UiHelper.showSnackbar
import com.devapp.fr.util.UiHelper.toGone
import com.devapp.fr.util.UiHelper.toVisible
import com.devapp.fr.util.animations.AnimationHelper.setOnClickWithAnimationListener
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenCreated
import com.devapp.fr.util.extensions.launchRepeatOnLifeCycleWhenResumed
import com.devapp.fr.util.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedbottompicker.TedBottomPicker
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class FragmentProfile : BaseFragment<FragmentProfileBinding>(), EasyPermissions.PermissionCallbacks {
    val TAG = "FragmentProfile"
    private val args: FragmentProfileArgs by navArgs()
    private lateinit var profileImageAdapter: ProfileImagesAdapter
    private val sharedViewModel:SharedViewModel by activityViewModels()
    private val storageViewModel:StorageViewModel by activityViewModels()
    private val fireStorageViewModel:AuthAndProfileViewModel by activityViewModels()
    private lateinit var listName:MutableList<String>
    private lateinit var listUri:MutableList<String>
    private var currentList = mutableListOf<String>()
    private var currentPos = -1
    private var isDelete = false

    override fun onSetupView() {

        listName = (requireActivity() as MainActivity).listName
        listUri = (requireActivity() as MainActivity).listUri
        updateUiProfile()
        setupProfileImageAdapter()
        handleOnBackPress()
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

    private fun setupProfileImageAdapter() {
        profileImageAdapter = ProfileImagesAdapter(this).apply {
            setOnItemClickListener { view, s ->
                requireActivity().sendImageToFullScreenImageActivity(view)
            }
            setOnLogItemClickListener { view, s,pos ->
                currentPos = pos
                UiHelper.triggerBottomAlertDialog(
                    requireActivity(),
                    "Xóa?",
                    "Bạn muốn xóa ảnh này ?",
                    "Đúng",
                    "Không",
                    false,
                    { dialogInterface, _ ->
                        if(!s.contains("firebase")){
                            storageViewModel.deleteImageByNameOrUri(args.id,true,listName[listUri.indexOf(s)],"")
                        }else{
                            storageViewModel.deleteImageByNameOrUri(args.id,false,"",s)
                        }
                        dialogInterface.dismiss()
                    }
                )
            }
        }
        binding.rcImage.apply {
            itemAnimator = SlideInLeftAnimator()
            adapter = profileImageAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        handleEvent()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun updateUiProfile() {
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
                            binding.apply {
                                lyLoading.toGone()
                                lyProfile.toVisible()
                            }
                            tvName.text = it[0] as String
                        }
                }
                launchRepeatOnLifeCycleWhenResumed{
                    sharedViewModel.getSharedFlowImage()
                        .distinctUntilChanged()
                        .collectLatest {
                            try{
                                currentList = it.toMutableList()
                                GlideApp.loadImage(currentList[0], icAvatar, this@FragmentProfile)
                                binding.tvImage.text = currentList.size.toString()
                                profileImageAdapter.submitList(currentList.toList())
                            }catch (e:Exception){
                                GlideApp.loadImage("", icAvatar, this@FragmentProfile)
                                binding.tvImage.setText("0")
                            }

                        }
                }
            }
                launchRepeatOnLifeCycleWhenCreated {
                    storageViewModel.stateAddImage
                        .collect {
                            when(it){
                                is ResourceRemote.Loading->{
                                    binding.root.showSnackbar("Đợi một chút ~")
                                }
                                is ResourceRemote.Success->{
                                    isDelete = false
                                    storageViewModel.downloadAllImagesById(args.id)

                                }
                                is ResourceRemote.Error->{
                                    showToast("Kiểm tra lại kết nối mạng!!")
                                }
                                else->{

                                }
                            }
                        }
                }

        launchRepeatOnLifeCycleWhenCreated {
            storageViewModel.stateDownloadAllImageById
                .collectLatest {
                    when(it){
                        is ResourceRemote.Loading->{
                            loadingDialog.show()
                        }
                        is ResourceRemote.Success->{
                            fireStorageViewModel.updateImagesUserProfile(args.id,it.data)
                        }
                        is ResourceRemote.Error->{
                            showToast("Kiểm tra lại kết nối mạng!!")
                        }
                        else->{

                        }
                    }
                }
        }
        lifecycleScope.launchWhenCreated {
            fireStorageViewModel.stateUpdateImagesUserProfile.collect {
                when(it){
                    is ResourceRemote.Loading->{
                    }

                    is ResourceRemote.Success->{
                        if(isDelete){
                            currentList.removeAt(currentPos)
                            binding.root.showSnackbar("Xoá thành công ~")
                        }else{
                            currentList.addAll(listUri)
                            binding.root.showSnackbar("Thêm thành công ~")
                        }
                        sharedViewModel.setSharedFlowImage(currentList.toList())
                        loadingDialog.dismiss()
                    }

                    is ResourceRemote.Error->{
                        Toast.makeText(requireContext(), "Có lỗi xảy ra ~", Toast.LENGTH_SHORT).show()
                        loadingDialog.dismiss()
                    }

                    is ResourceRemote.Empty->{
                        Log.d(TAG, "subscriberObserver: empty")
                    }

                    is ResourceRemote.Idle->{

                    }
                }
            }
        }

        launchRepeatOnLifeCycleWhenCreated {
            storageViewModel.stateDeleteImageByNameOrUri
                .collect {
                    when(it){
                        is ResourceRemote.Loading->{
                            binding.root.showSnackbar("Đợi một chút ~")
                        }
                        is ResourceRemote.Success->{
                            isDelete = true
                            storageViewModel.downloadAllImagesById(args.id)
                        }
                        is ResourceRemote.Error->{
                            showToast("Kiểm tra lại kết nối mạng!!")
                        }
                        else->{

                        }
                    }
                }
        }
    }

    private fun handleEvent() {
        binding.apply {
            btnBack.setOnClickWithAnimationListener {
                (requireActivity() as MainActivity).onBackPressed()
            }
            lyEditProfile.setOnClickWithAnimationListener {
                findNavController().navigate(
                    FragmentProfileDirections.actionFragmentProfileToFragmentEditProfile(
                        args.id
                    )
                )
            }
            lyAddImage.setOnClickWithAnimationListener {
                PermissionHelper.requestPermissionBottomPicker(this@FragmentProfile)
            }
            lyAvatar.setOnClickWithAnimationListener {
                requireActivity().sendImageToFullScreenImageActivity(it, profileImageAdapter.getItemAtPosition(0))
            }
        }
    }
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        openGallery()
    }

    private fun openGallery() {
        if (PermissionHelper.hasPermissionBottomPicker(requireContext())) {
            val tedBottomPicker =
                TedBottomPicker.with(requireActivity())
                    .setOnMultiImageSelectedListener {
                        if(it.isEmpty()) {
                            binding.root.showSnackbar("Chưa có ảnh được chọn ~")
                            return@setOnMultiImageSelectedListener
                        }
                        it.forEachIndexed{ index,data ->
                            listUri.add(data.toString())
                            listName.add(System.currentTimeMillis().toString()+"#"+index)
                        }
                        storageViewModel.addImagesById(args.id,listName,*it.toTypedArray())
                    }
                    .setOnErrorListener {
                        binding.root.showSnackbar(it)
                    }
                    .setTitle(R.string.select_image)
                    .setCompleteButtonText(R.string.selected_done)
                    .setEmptySelectionText("Trống")
                    .create()
            tedBottomPicker.show(childFragmentManager)
        } else {
            binding.root.showSnackbar("Need Permission")
        }
    }


    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            PermissionHelper.requestPermissionBottomPicker(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView: onDestroyView")
        fireStorageViewModel.resetStateUpdateImagesUserProfile()
        storageViewModel.resetStateAddImage()
        storageViewModel.resetStateDownloadAllImageById()
        storageViewModel.resetStateDeleteImageByNameOrUri()
        super.onDestroyView()
    }

}
