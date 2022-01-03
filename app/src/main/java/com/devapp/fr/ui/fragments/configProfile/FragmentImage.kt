package com.devapp.fr.ui.fragments.configProfile

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.devapp.fr.R
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.entities.UserProfile
import com.devapp.fr.databinding.FragmentImageBinding
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.activities.ConfigProfileActivity
import com.devapp.fr.ui.viewmodels.AuthViewModel
import com.devapp.fr.ui.viewmodels.StorageViewModel
import com.devapp.fr.util.CustomDialog
import com.devapp.fr.util.GlideApp
import com.devapp.fr.util.PermissionHelper
import com.devapp.fr.util.UiHelper.findOnClickListener
import com.devapp.fr.util.UiHelper.showSnackbar
import com.devapp.fr.util.UiHelper.toGone
import com.devapp.fr.util.animations.AnimationHelper.startAnimClick
import com.devapp.fr.util.storages.SharedPreferencesHelper
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedbottompicker.TedBottomPicker
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

@AndroidEntryPoint
class FragmentImage : BaseFragment<FragmentImageBinding>(), EasyPermissions.PermissionCallbacks {
    @Inject
    lateinit var prefs: SharedPreferencesHelper
    val TAG = "FragmentImage"
    private var isChooseImageOne: Boolean? = null
    private lateinit var job: Job
    private val authViewModel:AuthViewModel by viewModels()
    private val storageViewModel:StorageViewModel by viewModels()
    private lateinit var dialogLoading:CustomDialog
    private var listImage:MutableList<String> = mutableListOf("","")
    private var listUri:MutableList<Uri> = mutableListOf("".toUri(),"".toUri())
    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogLoading = CustomDialog(R.layout.dialog_loading)
    }

    override fun onSetupView() {
        findOnClickListener(
            binding.frImg1,
            binding.frImg2,
            binding.imgCamera1,
            binding.imgCamera2,
            binding.btnContinue
        ) {
            when (this) {
                binding.imgCamera1 -> {
                    PermissionHelper.requestPermissionBottomPicker(this@FragmentImage)
                    isChooseImageOne = true
                }
                binding.frImg1 -> {
                    PermissionHelper.requestPermissionBottomPicker(this@FragmentImage)
                    isChooseImageOne = true
                }

                binding.frImg2 -> {
                    PermissionHelper.requestPermissionBottomPicker(this@FragmentImage)
                    isChooseImageOne = false
                }
                binding.img2 -> {
                    PermissionHelper.requestPermissionBottomPicker(this@FragmentImage)
                    isChooseImageOne = false
                }

                binding.btnContinue -> {
                    this.startAnimClick()
                    storageViewModel.downloadAllImagesById("123", listOf("1641183258862","1641183259921"))
                }
            }
        }
    }

    private fun saveUserToFirebase() {
        val email = prefs.readEmail()
        val password = prefs.readPassword()
        val gender = prefs.readGender()
        val name = prefs.readNameConfig()
        val dob = prefs.readDobConfig()
        val address = prefs.readAddress()

        authViewModel.addUserProfile(
            UserProfile(
                email!!,
                password!!,
                name,
                gender,
                dob,
                address,
                id = UUID.randomUUID().toString()
            )
        )
    }

    override fun onDestroyView() {
        job.cancel()
        authViewModel.resetStateAddUserProfile()
        authViewModel.resetStateGetUserProfile()
        authViewModel.resetStateEmailExist()
        dialogLoading.onDestroyView()
        super.onDestroyView()
    }

    private fun openGallery() {
        if (PermissionHelper.hasPermissionBottomPicker(requireContext())) {
            val tedBottomPicker =
                TedBottomPicker.with(requireActivity())
                    .setOnImageSelectedListener {
                        GlideApp.loadImage(
                            it.toString(),
                            if (isChooseImageOne == true) binding.img1 else binding.img2,
                            this
                        )
                        if (isChooseImageOne == true) {
                            listImage[0] = "${System.currentTimeMillis()}"
                            listUri[0] = it
                            binding.imgCamera1.toGone()
                        } else {
                            binding.imgCamera2.toGone()
                            listImage[1] = "${System.currentTimeMillis()}"
                            listUri[1] = it
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
        } else {
            binding.root.showSnackbar("Need Permission")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        job = lifecycleScope.launch(Dispatchers.IO) {
//            while (true){
//                withContext(Dispatchers.Main){
//                    if (binding.img1.drawable == null || binding.img2.drawable == null)
//                        binding.btnContinue.enableOrNot(false) else binding.btnContinue.enableOrNot(true)
//                }
//            }
//            delay(500)
//        }

        subscriberObserver()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun subscriberObserver() {
        lifecycleScope.launchWhenResumed {
            authViewModel.stateAddUserProfile.collectLatest {
                when(it){
                    is ResourceRemote.Loading->{
                        dialogLoading.show(childFragmentManager,dialogLoading.tag)
                    }

                    is ResourceRemote.Success->{
                        binding.root.showSnackbar("Join app successfully~")
                        dialogLoading.dismiss()
                    }

                    is ResourceRemote.Error->{
                        dialogLoading.dismiss()
                        Log.d(TAG, "subscriberObserver: ${it.message}")
                    }

                    else->{

                    }
                }
            }
        }
        lifecycleScope.launchWhenResumed {
            authViewModel.stateGetUserProfile.collectLatest {
                when(it){
                    is ResourceRemote.Loading->{
                        dialogLoading.show(childFragmentManager,dialogLoading.tag)
                    }

                    is ResourceRemote.Success->{
                        dialogLoading.dismiss()
                        Log.d(TAG, "subscriberObserver: ${it.data?.email}")
                    }

                    is ResourceRemote.Error->{
                        dialogLoading.dismiss()
                        Log.d(TAG, "subscriberObserver: ${it.message}")
                    }

                    else->{
                        try{
                            dialogLoading.dismiss()
                        }catch (e:Exception){

                        }
                    }
                }
            }
        }
        lifecycleScope.launchWhenResumed {
            authViewModel.stateEmailExist.collectLatest {
                when(it){
                    is ResourceRemote.Loading->{
                        dialogLoading.show(childFragmentManager,dialogLoading.tag)
                    }

                    is ResourceRemote.Success->{
                        Log.d(TAG, "subscriberObserver: ${it.data}")
                        dialogLoading.dismiss()
                    }

                    is ResourceRemote.Error->{
                        dialogLoading.dismiss()
                        Log.d(TAG, "subscriberObserver: ${it.message}")
                    }

                    is ResourceRemote.Empty->{
                        Log.d(TAG, "subscriberObserver: empty")
                    }

                    is ResourceRemote.Idle->{

                    }
                }
            }
        }
        lifecycleScope.launchWhenResumed {
            storageViewModel.stateAddImage.collectLatest {
                when(it){
                    is ResourceRemote.Loading->{
                        dialogLoading.show(childFragmentManager,dialogLoading.tag)
                    }

                    is ResourceRemote.Success->{
                        Log.d(TAG, "subscriberObserver: ${it.data}")
                        dialogLoading.dismiss()
                    }

                    is ResourceRemote.Error->{
                        dialogLoading.dismiss()
                        Log.d(TAG, "subscriberObserver: ${it.message}")
                    }

                    is ResourceRemote.Empty->{
                        Log.d(TAG, "subscriberObserver: empty")
                    }

                    is ResourceRemote.Idle->{

                    }
                }
            }
        }
        lifecycleScope.launchWhenResumed {
            storageViewModel.stateDownloadAllImageById.collectLatest {
                when(it){
                    is ResourceRemote.Loading->{
                        dialogLoading.show(childFragmentManager,dialogLoading.tag)
                    }

                    is ResourceRemote.Success->{
                        binding.apply {
                            img1.setImageBitmap(it.data[0])
                            img2.setImageBitmap(it.data[1])
                        }
                        dialogLoading.dismiss()
                    }

                    is ResourceRemote.Error->{
                        dialogLoading.dismiss()
                        Log.d(TAG, "subscriberObserver: ${it.message}")
                    }

                    is ResourceRemote.Empty->{
                        Log.d(TAG, "subscriberObserver: empty")
                    }

                    is ResourceRemote.Idle->{

                    }
                }
            }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        openGallery()
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
}