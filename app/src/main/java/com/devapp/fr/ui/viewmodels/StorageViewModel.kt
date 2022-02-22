package com.devapp.fr.ui.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.fr.di.IoDispatcher
import com.devapp.fr.network.ResourceRemote
import com.devapp.fr.network.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(
    app: Application,
    private val storage: StorageService,
    @IoDispatcher private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    AndroidViewModel(app) {

    //add image
    private val _stateAddImage: MutableStateFlow<ResourceRemote<String>> = MutableStateFlow(
        ResourceRemote.Idle
    )
    val stateAddImage: StateFlow<ResourceRemote<String>> = _stateAddImage
    fun resetStateAddImage() {
        _stateAddImage.value = ResourceRemote.Idle
    }

    fun addImagesById(id: String, listImageName: List<String>, vararg uriImage: Uri) {
        _stateAddImage.value = ResourceRemote.Loading
        viewModelScope.launch(defaultDispatcher) {
            _stateAddImage.value =
                storage.addImagesById(id, Dispatchers.IO, listImageName, *uriImage)
        }
    }

    //add image
    private val _stateDeleteImageByNameOrUri: MutableStateFlow<ResourceRemote<Boolean>> =
        MutableStateFlow(ResourceRemote.Idle)
    val stateDeleteImageByNameOrUri: StateFlow<ResourceRemote<Boolean>> =
        _stateDeleteImageByNameOrUri

    fun resetStateDeleteImageByNameOrUri() {
        _stateDeleteImageByNameOrUri.value = ResourceRemote.Idle
    }

    fun deleteImageByNameOrUri(
        id: String,
        isByName: Boolean,
        nameFile: String = "",
        url: String = ""
    ) {
        _stateDeleteImageByNameOrUri.value = ResourceRemote.Loading
        viewModelScope.launch(defaultDispatcher) {
            _stateDeleteImageByNameOrUri.value =
                storage.deleteImageByNameOrUrl(id, isByName, nameFile, url)
        }
    }

    //download all image
    private val _stateDownloadAllImageById: MutableStateFlow<ResourceRemote<List<String>>> =
        MutableStateFlow(
            ResourceRemote.Idle
        )
    val stateDownloadAllImageById: StateFlow<ResourceRemote<List<String>>> =
        _stateDownloadAllImageById

    fun resetStateDownloadAllImageById() {
        _stateDownloadAllImageById.value = ResourceRemote.Idle
    }

    fun downloadAllImagesById(id: String) {
        _stateDownloadAllImageById.value = ResourceRemote.Loading
        viewModelScope.launch(defaultDispatcher) {
            _stateDownloadAllImageById.value = storage.downloadAllImagesById(id)
        }
    }

    //download all image
    private val _stateAddImageChats: MutableStateFlow<String?> = MutableStateFlow(null)
    val stateAddImageChats: StateFlow<String?> = _stateAddImageChats
    fun resetStateAddImageChats() {
        _stateAddImageChats.value = null
    }

    fun addImageChat(
        senderRoom: String,
        recieverRoom: String,
        uriImage: Uri,
    ) {
        viewModelScope.launch(defaultDispatcher) {
            storage.addImageIntoStorageChats(senderRoom, recieverRoom, uriImage, {
                _stateAddImageChats.value = it
            }, { _stateAddImageChats.value = null })
        }
    }

    private val _stateAddAudio: MutableStateFlow<String?> = MutableStateFlow(null)
    val stateAddAudio: StateFlow<String?> = _stateAddAudio
    fun resetStateAddAudio() {
        _stateAddAudio.value = null
    }

    fun addAudio(
        senderRoom: String,
        recieverRoom: String,
        uriAudio: Uri,
    ) {
        viewModelScope.launch(defaultDispatcher) {
            storage.addAudioIntoStorageAudio(senderRoom, recieverRoom, uriAudio, {
                _stateAddAudio.value = it
            }, { _stateAddAudio.value = null })
        }
    }
}