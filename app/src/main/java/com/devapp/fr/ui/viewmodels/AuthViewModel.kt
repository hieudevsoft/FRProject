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

    //add user profile
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

    //read user profile
    private val _stateGetUserProfile:MutableStateFlow<ResourceRemote<UserProfile?>> = MutableStateFlow(ResourceRemote.Idle)
    val stateGetUserProfile:StateFlow<ResourceRemote<UserProfile?>> = _stateGetUserProfile
    fun resetStateGetUserProfile() {
        _stateGetUserProfile.value = ResourceRemote.Idle
    }
    fun getUserProfile(id:String){
        _stateGetUserProfile.value = ResourceRemote.Loading
        viewModelScope.launch {
            _stateGetUserProfile.value = fireStoreService.getUserProfile(id)
        }
    }

    //email is exist?
    //read user profile
    private val _stateEmailExist:MutableStateFlow<ResourceRemote<String>> = MutableStateFlow(ResourceRemote.Idle)
    val stateEmailExist:StateFlow<ResourceRemote<String>> = _stateEmailExist
    fun resetStateEmailExist() {
        _stateGetUserProfile.value = ResourceRemote.Idle
    }
    fun isEmailExits(email:String){
        _stateEmailExist.value = ResourceRemote.Loading
        viewModelScope.launch {
            _stateEmailExist.value = fireStoreService.isEmailExist(email)
        }
    }

    //email is exist?
    //update images user profile
    private val _stateUpdateImagesUserProfile:MutableStateFlow<ResourceRemote<Boolean>> = MutableStateFlow(ResourceRemote.Idle)
    val stateUpdateImagesUserProfile:StateFlow<ResourceRemote<Boolean>> = _stateUpdateImagesUserProfile
    fun resetStateImagesUserProfile() {
        _stateUpdateImagesUserProfile.value = ResourceRemote.Idle
    }
    fun updateImagesUserProfile(email:String,listImage:List<String>){
        _stateUpdateImagesUserProfile.value = ResourceRemote.Loading
        viewModelScope.launch {
            _stateUpdateImagesUserProfile.value = fireStoreService.updateImages(email,listImage)
        }
    }

    //login
    private val _stateLogin:MutableStateFlow<ResourceRemote<String>> = MutableStateFlow(ResourceRemote.Idle)
    val stateLogin:StateFlow<ResourceRemote<String>> = _stateLogin
    fun resetStateLogin() {
        _stateLogin.value = ResourceRemote.Idle
    }
    fun loginWitEmailAndPassword(email:String,password:String){
        _stateEmailExist.value = ResourceRemote.Loading
        viewModelScope.launch {
            _stateEmailExist.value = fireStoreService.loginWithEmailAndPassword(email,password)
        }
    }

}