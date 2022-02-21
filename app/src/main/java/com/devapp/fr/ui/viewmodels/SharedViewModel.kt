package com.devapp.fr.ui.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.devapp.fr.adapters.InformationAdapter
import com.devapp.fr.data.entities.UserProfile
import com.devapp.fr.data.models.items.InformationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

val KEY_SCROLL_POSITION = "key.scroll.position"

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val savedStateHandle:SavedStateHandle,
    private val app:Application):AndroidViewModel(app) {

    var currentNestedScrollPosition = 0
    set(value) {
        field = value
        savedStateHandle.set(KEY_SCROLL_POSITION,value)
    }
    init {
        currentNestedScrollPosition = savedStateHandle.get<Int>(KEY_SCROLL_POSITION)?:0
    }

    private val _positionInformation:MutableLiveData<Int> = MutableLiveData()
    fun getPositionInformation():LiveData<Int> = _positionInformation
    fun setPositionInformation(value:Int){
        viewModelScope.launch {
            _positionInformation.postValue(value)
        }
    }

    private val _positionMainViewPager:MutableLiveData<Int> = MutableLiveData()
    fun getPositionMainViewPager():LiveData<Int> = _positionMainViewPager
    fun setPositionMainViewPager(value:Int){
        viewModelScope.launch {
            _positionMainViewPager.postValue(value)
        }
    }

    private val _listItemInformation:MutableLiveData<List<InformationItem>> = MutableLiveData()
    fun getListItemInformation():LiveData<List<InformationItem>> = _listItemInformation
    fun setListItemInformation(value:List<InformationItem>){
        viewModelScope.launch {
            _listItemInformation.postValue(value)
        }
    }

    private val _sharedFlowTall:MutableSharedFlow<Int> = MutableSharedFlow(1)
    fun getSharedFlowTall() = _sharedFlowTall.asSharedFlow()
    fun setSharedFlowTall(value:Int){
        viewModelScope.launch {
            _sharedFlowTall.emit(value)
        }
    }

    private val _sharedFlowChild:MutableSharedFlow<Int> = MutableSharedFlow(1)
    fun getSharedFlowChild() = _sharedFlowChild.asSharedFlow()
    fun setSharedFlowChild(value:Int){
        viewModelScope.launch {
            _sharedFlowChild.emit(value)
        }
    }

    private val _sharedFlowDrink:MutableSharedFlow<Int> = MutableSharedFlow(1)
    fun getSharedFlowDrink() = _sharedFlowDrink.asSharedFlow()
    fun setSharedFlowDrink(value:Int){
        viewModelScope.launch {
            _sharedFlowDrink.emit(value)
        }
    }

    private val _sharedFlowMaritalStatus:MutableSharedFlow<Int> = MutableSharedFlow(1)
    fun getSharedFlowMaritalStatus() = _sharedFlowMaritalStatus.asSharedFlow()
    fun setSharedFlowMaritalStatus(value:Int){
        viewModelScope.launch {
            _sharedFlowMaritalStatus.emit(value)
        }
    }

    private val _sharedFlowChooseGender:MutableSharedFlow<Int> = MutableSharedFlow(1)
    fun getSharedFlowChooseGender() = _sharedFlowChooseGender.asSharedFlow()
    fun setSharedFlowChooseGender(value:Int){
        viewModelScope.launch {
            _sharedFlowChooseGender.emit(value)
        }
    }

    private val _sharedFlowSmoke:MutableSharedFlow<Int> = MutableSharedFlow(1)
    fun getSharedFlowSmoke() = _sharedFlowSmoke.asSharedFlow()
    fun setSharedFlowSmoke(value :Int){
        viewModelScope.launch {
            _sharedFlowSmoke.emit(value)
        }
    }

    private val _sharedFlowPet:MutableSharedFlow<Int> = MutableSharedFlow(1)
    fun getSharedFlowPet() = _sharedFlowPet.asSharedFlow()
    fun setSharedFlowPet(value :Int){
        viewModelScope.launch {
            _sharedFlowPet.emit(value)
        }
    }

    private val _sharedFlowReligion:MutableSharedFlow<Int> = MutableSharedFlow(1)
    fun getSharedFlowReligion() = _sharedFlowReligion.asSharedFlow()
    fun setSharedFlowReligion(value :Int){
        viewModelScope.launch {
            _sharedFlowReligion.emit(value)
        }
    }

    private val _sharedFlowCertificate:MutableSharedFlow<Int> = MutableSharedFlow(1)
    fun getSharedFlowCertificate() = _sharedFlowCertificate.asSharedFlow()
    fun setSharedFlowCertificate(value :Int){
        viewModelScope.launch {
            _sharedFlowCertificate.emit(value)
        }
    }

    private val _sharedFlowIntroduce:MutableSharedFlow<String> = MutableSharedFlow(1)
    fun getSharedFlowIntroduce() = _sharedFlowIntroduce.asSharedFlow()
    fun setSharedFlowIntroduce(value:String){
        viewModelScope.launch {
            _sharedFlowIntroduce.emit(value)
        }
    }

    private val _sharedFlowInterest:MutableSharedFlow<List<Int>> = MutableSharedFlow(1)
    fun getSharedFlowInterest() = _sharedFlowInterest.asSharedFlow()
    fun setSharedFlowInterest(list:List<Int>){
        viewModelScope.launch {
            _sharedFlowInterest.emit(list)
        }
    }

    private val _sharedFlowSexuality:MutableSharedFlow<Int> = MutableSharedFlow(1)
    fun getSharedFlowSexuality() = _sharedFlowSexuality.asSharedFlow()
    fun setSharedFlowSexuality(value:Int){
        viewModelScope.launch {
            _sharedFlowSexuality.emit(value)
        }
    }

    private val _sharedPersonality:MutableSharedFlow<Int> = MutableSharedFlow(1)
    fun getSharedPersonality() = _sharedPersonality.asSharedFlow()
    fun setSharedPersonality(value :Int){
        viewModelScope.launch {
            _sharedPersonality.emit(value)
        }
    }

    private val _sharedFlowJob:MutableSharedFlow<String> = MutableSharedFlow(1)
    fun getSharedFlowJob() = _sharedFlowJob.asSharedFlow()
    fun setSharedFlowJob(value:String){
        viewModelScope.launch {
            _sharedFlowJob.emit(value)
        }
    }

    private val _sharedFlowBasicInformation:MutableSharedFlow<HashMap<Int,Any>> = MutableSharedFlow(1)
    fun getSharedFlowBasicInformation() = _sharedFlowBasicInformation.asSharedFlow()
    fun setSharedFlowBasicInformation(value:HashMap<Int,Any>){
        viewModelScope.launch {
            _sharedFlowBasicInformation.emit(value)
        }
    }

    private val _sharedFlowImage:MutableSharedFlow<List<String>> = MutableSharedFlow(2)
    fun getSharedFlowImage() = _sharedFlowImage.asSharedFlow()
    fun setSharedFlowImage(value:List<String>){
        viewModelScope.launch {
            _sharedFlowImage.emit(value)
        }
    }

    private val _sharedFlowListUserWaitingAccept:MutableSharedFlow<List<UserProfile>> = MutableSharedFlow(1)
    fun getSharedFlowListUserWaitingAccept() = _sharedFlowListUserWaitingAccept.asSharedFlow()
    fun setSharedFlowListUserWaitingAccept(list:List<UserProfile>){
        viewModelScope.launch {
            _sharedFlowListUserWaitingAccept.emit(list)
        }
    }

    private val _sharedFlowListUserMatch:MutableSharedFlow<List<UserProfile>> = MutableSharedFlow(1)
    fun getSharedFlowListUserMatch() = _sharedFlowListUserMatch.asSharedFlow()
    fun setSharedFlowListUserMatch(list:List<UserProfile>){
        viewModelScope.launch {
            _sharedFlowListUserMatch.emit(list)
        }
    }

    private val _sharedFlowListUserMatchByMe:MutableSharedFlow<List<UserProfile>> = MutableSharedFlow(1)
    fun getSharedFlowListUserMatchByMe() = _sharedFlowListUserMatchByMe.asSharedFlow()
    fun setSharedFlowListUserMatchByMe(list:List<UserProfile>){
        viewModelScope.launch {
            _sharedFlowListUserMatchByMe.emit(list)
        }
    }



}