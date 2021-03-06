package com.devapp.fr.ui.fragments.configProfile

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.devapp.fr.R
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.data.entities.UserProfile
import com.devapp.fr.databinding.FragmentImageBinding
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.ui.viewmodels.AuthAndProfileViewModel
import com.devapp.fr.ui.viewmodels.StorageViewModel
import com.devapp.fr.util.Constants
import com.devapp.fr.ui.widgets.LoadingDialog
import com.devapp.fr.util.GlideApp
import com.devapp.fr.util.PermissionHelper
import com.devapp.fr.util.UiHelper.enableOrNot
import com.devapp.fr.util.UiHelper.findOnClickListener
import com.devapp.fr.util.UiHelper.showSnackbar
import com.devapp.fr.util.UiHelper.toGone
import com.devapp.fr.util.animations.AnimationHelper.startAnimClick
import com.devapp.fr.util.storages.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import gun0912.tedbottompicker.TedBottomPicker
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class FragmentImage : BaseFragment<FragmentImageBinding>(), EasyPermissions.PermissionCallbacks {
    @Inject
    lateinit var prefs: SharedPreferencesHelper
    val TAG = "FragmentImage"
    private var isChooseImageOne: Boolean? = null
    private lateinit var job: Job
    private val authViewModel:AuthAndProfileViewModel by viewModels()
    private val storageViewModel:StorageViewModel by viewModels()

    private var listImage:MutableList<String> = mutableListOf("","")
    private var listUri:MutableList<Uri> = mutableListOf("".toUri(),"".toUri())
    private var idOfUser:String=""
    override fun onAttach(context: Context) {
        super.onAttach(context)

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
                    prefs.saveProcessRegister(6)
                    idOfUser = UUID.randomUUID().toString()
                    storageViewModel.addImagesById(idOfUser,listImage,listUri[0],listUri[1])
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
                email=email!!,
                password = password!!,
                name = name,
                gender = gender,
                dob = dob,
                address = address,
                id = idOfUser
            )
        )
    }

    override fun onDestroyView() {
        job.cancel()
        authViewModel.resetStateAddUserProfile()
        storageViewModel.resetStateAddImage()
        storageViewModel.resetStateDownloadAllImageById()
        authViewModel.resetStateUpdateImagesUserProfile()
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
                    .setEmptySelectionText("Tr???ng")
                    .create()
            tedBottomPicker.show(childFragmentManager)
        } else {
            binding.root.showSnackbar("Need Permission")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        statusButtonContinue()
        subscriberObserver()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun statusButtonContinue() {
        job = lifecycleScope.launch(Dispatchers.IO) {
            while (true){
                withContext(Dispatchers.Main){
                    if (binding.img1.drawable == null || binding.img2.drawable == null)
                        binding.btnContinue.enableOrNot(false) else binding.btnContinue.enableOrNot(true)
                }
            }
            delay(Constants.DEFAULT_DELAY_STATUS.toLong())
        }
    }

    private fun subscriberObserver() {
        lifecycleScope.launchWhenResumed {
            storageViewModel.stateAddImage.collectLatest {
                when(it){
                    is ResourceRemote.Loading->{
                        loadingDialog.show()
                    }

                    is ResourceRemote.Success->{
                        saveUserToFirebase()
                    }

                    is ResourceRemote.Error->{

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
            authViewModel.stateAddUserProfile.collectLatest {
                when(it){
                    is ResourceRemote.Loading->{

                    }

                    is ResourceRemote.Success->{
                        Log.d(TAG, "subscriberObserver: save successfully")
                        storageViewModel.downloadAllImagesById(idOfUser)

                    }

                    is ResourceRemote.Error->{

                        Log.d(TAG, "subscriberObserver: ${it.message}")
                    }

                    else->{

                    }
                }
            }
        }
        lifecycleScope.launchWhenResumed {
            storageViewModel.stateDownloadAllImageById.collectLatest {
                when(it){
                    is ResourceRemote.Loading->{
                    }

                    is ResourceRemote.Success->{
                        authViewModel.updateImagesUserProfile(idOfUser,it.data)
                    }

                    is ResourceRemote.Error->{
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
        lifecycleScope.launchWhenResumed {
            authViewModel.stateUpdateImagesUserProfile.collect {
                when(it){
                    is ResourceRemote.Loading->{
                    }

                    is ResourceRemote.Success->{
                        Log.d(TAG, "subscriberObserver: update images successfully")
                        Handler(Looper.getMainLooper()).postDelayed({
                            loadingDialog.dismiss()
                            findNavController().navigate(FragmentImageDirections.actionFragmentImageToFragmentTerms())
                            resetStateLocal()
                        },1000)

                    }

                    is ResourceRemote.Error->{
                        Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                        loadingDialog.dismiss()
                    }

                    is ResourceRemote.Empty->{
                        loadingDialog.dismiss()
                        Log.d(TAG, "subscriberObserver: empty")
                    }

                    is ResourceRemote.Idle->{
                        loadingDialog.dismiss()

                    }
                }
            }
        }
    }

    private fun resetStateLocal() {
        prefs.saveProcessRegister(7)
        prefs.saveAddress("")
        prefs.saveNameConfig("")
        prefs.saveGender(0)
        prefs.saveEmail("")
        prefs.saveDobConfig("")
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