package com.devapp.fr.ui.fragments.configProfile

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.devapp.fr.R
import com.devapp.fr.app.BaseFragment
import com.devapp.fr.databinding.FragmentImageBinding
import com.devapp.fr.ui.activities.ConfigProfileActivity
import com.devapp.fr.util.GlideApp
import com.devapp.fr.util.PermissionHelper
import com.devapp.fr.util.UiHelper.enableOrNot
import com.devapp.fr.util.UiHelper.findOnClickListener
import com.devapp.fr.util.UiHelper.showSnackbar
import com.devapp.fr.util.UiHelper.toGone
import com.devapp.fr.util.storages.SharedPreferencesHelper
import gun0912.tedbottompicker.TedBottomPicker
import kotlinx.coroutines.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

class FragmentImage : BaseFragment<FragmentImageBinding>(), EasyPermissions.PermissionCallbacks {
    private lateinit var prefs: SharedPreferencesHelper
    val TAG = "FragmentImage"
    private var isChooseImageOne: Boolean? = null
    private lateinit var job: Job
    override fun onAttach(context: Context) {
        super.onAttach(context)
        prefs = (context as ConfigProfileActivity).sharedPrefs
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

                }
            }
        }
    }

    override fun onDestroyView() {
        job.cancel()
        super.onDestroyView()
    }

    private fun openGallary() {
        if (PermissionHelper.hasPermissionBottomPicker(requireContext())) {
            val tedBottomPicker =
                TedBottomPicker.with(requireActivity())
                    .setOnImageSelectedListener {
                        GlideApp.loadImage(
                            it.toString(),
                            if (isChooseImageOne == true) binding.img1 else binding.img2,
                            this
                        )
                        if (isChooseImageOne == true) binding.imgCamera1.toGone() else binding.imgCamera2.toGone()
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
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        openGallary()
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