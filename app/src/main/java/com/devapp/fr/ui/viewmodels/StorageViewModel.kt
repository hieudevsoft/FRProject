package com.devapp.fr.ui.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.network.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(app:Application, private val storage:StorageService):AndroidViewModel(app) {

    //add image
    private val _stateAddImage: MutableStateFlow<ResourceRemote<String>> = MutableStateFlow(
        ResourceRemote.Idle)
    val stateAddImage: StateFlow<ResourceRemote<String>> = _stateAddImage
    fun resetStateAddImage() {
        _stateAddImage.value = ResourceRemote.Idle
    }
    fun addImage(id: String,uriImage:Uri){
        _stateAddImage.value = ResourceRemote.Loading
        viewModelScope.launch {
            _stateAddImage.value = storage.addImage(id,uriImage)
        }
    }

    //download all image
    private val _stateDownloadAllImageById: MutableStateFlow<ResourceRemote<List<String>>> = MutableStateFlow(
        ResourceRemote.Idle)
    val stateDownloadAllImageById: StateFlow<ResourceRemote<List<String>>> = _stateDownloadAllImageById
    fun resetStateDownloadAllImageById() {
        _stateDownloadAllImageById.value = ResourceRemote.Idle
    }
    fun downloadAllImagesById(id: String){
        _stateDownloadAllImageById.value = ResourceRemote.Loading
        viewModelScope.launch {
            _stateDownloadAllImageById.value = storage.downloadAllImagesById(id)
        }
    }
}