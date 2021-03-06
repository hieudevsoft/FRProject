package com.devapp.fr.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.fr.data.entities.UserProfile
import com.devapp.fr.di.IoDispatcher
import com.devapp.fr.network.FireStoreService
import com.devapp.fr.network.ResourceRemote
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthAndProfileViewModel @Inject constructor(
    private val app:Application,
    private val fireStoreService:FireStoreService,
    @IoDispatcher private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
):AndroidViewModel(app) {

    //add user profile
    private val _stateAddUserProfile:MutableStateFlow<ResourceRemote<String>> = MutableStateFlow(ResourceRemote.Idle)
    val stateAddUserProfile:StateFlow<ResourceRemote<String>> = _stateAddUserProfile
    fun resetStateAddUserProfile() {
        _stateAddUserProfile.value = ResourceRemote.Idle
    }
    fun addUserProfile(userProfile:UserProfile){
        _stateAddUserProfile.value = ResourceRemote.Loading
        viewModelScope.launch(defaultDispatcher) {
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
        viewModelScope.launch(defaultDispatcher) {
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
        viewModelScope.launch(defaultDispatcher) {
            _stateEmailExist.value = fireStoreService.isEmailExist(email)
        }
    }

    //email is exist?
    //update images user profile
    private val _stateUpdateImagesUserProfile:MutableStateFlow<ResourceRemote<Boolean>> = MutableStateFlow(ResourceRemote.Idle)
    val stateUpdateImagesUserProfile:StateFlow<ResourceRemote<Boolean>> = _stateUpdateImagesUserProfile
    fun resetStateUpdateImagesUserProfile() {
        _stateUpdateImagesUserProfile.value = ResourceRemote.Idle
    }
    fun updateImagesUserProfile(id:String,listImage:List<String>){
        _stateUpdateImagesUserProfile.value = ResourceRemote.Loading
        viewModelScope.launch(defaultDispatcher) {
            _stateUpdateImagesUserProfile.value = fireStoreService.updateImages(id,listImage)
        }
    }

    //login
    private val _stateLogin:MutableStateFlow<ResourceRemote<String>> = MutableStateFlow(ResourceRemote.Idle)
    val stateLogin:StateFlow<ResourceRemote<String>> = _stateLogin
    fun resetStateLogin() {
        _stateLogin.value = ResourceRemote.Idle
    }
    fun loginWitEmailAndPassword(email:String,password:String){
        _stateLogin.value = ResourceRemote.Loading
        viewModelScope.launch(defaultDispatcher) {
            _stateLogin.value = fireStoreService.loginWithEmailAndPassword(email,password)
        }
    }

    //update basic information
    private val _stateBasicInformation:MutableStateFlow<ResourceRemote<Boolean>> = MutableStateFlow(ResourceRemote.Idle)
    val stateBasicInformation:StateFlow<ResourceRemote<Boolean>> = _stateBasicInformation
    fun resetStateBasicInformation() {
        _stateBasicInformation.value = ResourceRemote.Idle
    }
    fun updateBasicInformation(id:String,mapBasicInformation:HashMap<Int,Any>){
        _stateBasicInformation.value = ResourceRemote.Loading
        viewModelScope.launch(defaultDispatcher) {
            _stateBasicInformation.value = fireStoreService.updateBasicInformation(id,mapBasicInformation)
        }
    }

    //update by field name
    private val _stateFieldName:MutableStateFlow<ResourceRemote<Boolean>> = MutableStateFlow(ResourceRemote.Idle)
    val stateFieldName:StateFlow<ResourceRemote<Boolean>> = _stateFieldName
    fun resetStateFieldName() {
        _stateFieldName.value = ResourceRemote.Idle
    }
    fun <T> updateByFiledName(id:String,fieldName:String,data:T){
        _stateFieldName.value = ResourceRemote.Loading
        viewModelScope.launch(defaultDispatcher) {
            _stateFieldName.value = fireStoreService.updateFieldByName(id,fieldName,data)
        }
    }

    //update additional information by field name
    private val _stateAdditionalInformation:MutableStateFlow<ResourceRemote<Boolean>> = MutableStateFlow(ResourceRemote.Idle)
    val stateAdditionalInformation:StateFlow<ResourceRemote<Boolean>> = _stateAdditionalInformation
    fun resetStateAdditionalInformation() {
        _stateAdditionalInformation.value = ResourceRemote.Idle
    }
    fun updateAdditionalInformation(id:String,fieldName:String,data:Int){
        _stateAdditionalInformation.value = ResourceRemote.Loading
        viewModelScope.launch(defaultDispatcher) {
            _stateAdditionalInformation.value = fireStoreService.updateFieldAdditionalInformation(id,fieldName,data)
        }
    }

    //get all profile by not equals gender,id and limit result
    private val _sateGetAllProfileSwipe:MutableStateFlow<ResourceRemote<List<UserProfile>>> = MutableStateFlow(ResourceRemote.Idle)
    val sateGetAllProfileSwipe:StateFlow<ResourceRemote<List<UserProfile>>> = _sateGetAllProfileSwipe
    fun resetSateGetAllProfileSwipe() {
        _sateGetAllProfileSwipe.value = ResourceRemote.Idle
    }
    fun getAllProfileSwipe(ids:List<String>,gender:Int,limit:Long){
        _sateGetAllProfileSwipe.value = ResourceRemote.Loading
        viewModelScope.launch(defaultDispatcher) {
            _sateGetAllProfileSwipe.value = fireStoreService.getAllUserProfileByLimit(ids, gender,limit)
        }
    }

    //get all profile by not equals gender,id and limit result
    private val _sateSendNotificationToPartner:MutableStateFlow<ResourceRemote<String>> = MutableStateFlow(ResourceRemote.Idle)
    val sateSendNotificationToPartner:StateFlow<ResourceRemote<String>> = _sateSendNotificationToPartner
    fun resetSateSendNotificationToPartner() {
        _sateSendNotificationToPartner.value = ResourceRemote.Idle
    }
    fun sendNotificationsToPartner(partnerId:String,ownerId:String){
        _sateSendNotificationToPartner.value = ResourceRemote.Loading
        viewModelScope.launch(defaultDispatcher) {
            _sateSendNotificationToPartner.value = fireStoreService.sendNotificationForPartner(partnerId,ownerId)
        }
    }

    //get all profile by owner waiting accept
    fun getAllProfileWaitingAccept(id:String,snapshotCallBack:(List<UserProfile>)->Unit){
        viewModelScope.launch(defaultDispatcher) {
            fireStoreService.getAllUserWaitingAcceptById(id,Dispatchers.IO,snapshotCallBack)
        }
    }

    //get all profile by owner accept or cancel match
    fun getAllProfileMatch(id:String,snapshotCallBack:(List<UserProfile>)->Unit){
        viewModelScope.launch(defaultDispatcher) {
            fireStoreService.getAllUserSendNotificationToMe(id,Dispatchers.IO,snapshotCallBack)
        }
    }

    //get all profile by owner match
    fun getAllUserMatch(id:String,snapshotCallBack:(List<UserProfile>)->Unit){
        viewModelScope.launch(defaultDispatcher) {
            fireStoreService.getAllUserMatchesMe(id,Dispatchers.IO,snapshotCallBack)
        }
    }

    //accept or cancel Notification
    private val _stateAcceptOrCancel:MutableStateFlow<ResourceRemote<String>> = MutableStateFlow(ResourceRemote.Idle)
    val stateAcceptOrCancel:StateFlow<ResourceRemote<String>> = _stateAcceptOrCancel
    fun resetSateAcceptOrCancel() {
        _stateAcceptOrCancel.value = ResourceRemote.Idle
    }
    fun acceptOrCancel(partnerId:String,ownerId:String,isAccept:Boolean){
        _stateAcceptOrCancel.value = ResourceRemote.Loading
        viewModelScope.launch(defaultDispatcher) {
            _stateAcceptOrCancel.value = fireStoreService.acceptOrCancelInviteMatch(partnerId,ownerId,isAccept)
        }
    }

    private val _sateGetAllProfileSwipePersonality:MutableStateFlow<ResourceRemote<List<UserProfile>>> = MutableStateFlow(ResourceRemote.Idle)
    val sateGetAllProfileSwipePersonality:StateFlow<ResourceRemote<List<UserProfile>>> = _sateGetAllProfileSwipePersonality
    fun resetSateGetAllProfileSwipePersonality() {
        _sateGetAllProfileSwipePersonality.value = ResourceRemote.Idle
    }
    fun getAllProfileSwipePersonality(ids:List<String>,personality:Int,limit:Long){
        _sateGetAllProfileSwipePersonality.value = ResourceRemote.Loading
        viewModelScope.launch(defaultDispatcher) {
            _sateGetAllProfileSwipePersonality.value = fireStoreService.getAllUserProfileByPersonality(ids,personality,limit)
        }
    }
}