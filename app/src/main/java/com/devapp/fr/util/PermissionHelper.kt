package com.devapp.fr.util

import android.Manifest
import android.content.Context
import com.devapp.fr.util.Constants.RC_MEDIA
import pub.devrel.easypermissions.EasyPermissions
import androidx.fragment.app.Fragment
import com.devapp.fr.util.Constants.RC_AUDIO

object PermissionHelper {

    fun hasPermissionBottomPicker(context:Context):Boolean{
        return EasyPermissions.hasPermissions(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
        )
    }

    fun requestPermissionBottomPicker(fragment: Fragment){
        EasyPermissions.requestPermissions(
            fragment,
            "Bạn phải thông qua những quyền này để thực hiện~",
            RC_MEDIA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
    }

    fun hasPermissionAudio(context:Context):Boolean{
        return EasyPermissions.hasPermissions(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
        )
    }

    fun requestPermissionAudio(fragment: Fragment){
        EasyPermissions.requestPermissions(
            fragment,
            "Bạn phải thông qua những quyền này để thực hiện~",
            RC_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
        )
    }
}