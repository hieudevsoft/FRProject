package com.devapp.fr.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.fr.data.entities.UserProfile
import com.devapp.fr.network.FireStoreService
import com.devapp.fr.network.ResourceRemote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val app:Application,
    private val fireStoreService:FireStoreService
):AndroidViewModel(app) {

    private val _stateAddUserProfile:MutableStateFlow<ResourceRemote<String>> = MutableStateFlow(ResourceRemote.Idle)
    val stateAddUserProfile:StateFlow<ResourceRemote<String>> = _stateAddUserProfile
    fun resetStateAddUserProfile() {
        _stateAddUserProfile.value = ResourceRemote.Idle
    }

    fun addUserProfile(userProfile:UserProfile){
        _stateAddUserProfile.value = ResourceRemote.Loading
        viewModelScope.launch {
            _stateAddUserProfile.value = fireStoreService.addUserProfile(userProfile)
      }
    }
}