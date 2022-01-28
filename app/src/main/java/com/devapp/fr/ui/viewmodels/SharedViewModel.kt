package com.devapp.fr.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.devapp.fr.adapters.InformationAdapter
import com.devapp.fr.data.models.items.InformationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val app:Application):AndroidViewModel(app) {

    private val _positionInformation:MutableLiveData<Int> = MutableLiveData()
    fun getPositionInformation():LiveData<Int> = _positionInformation
    fun setPositionInformation(value:Int){
        viewModelScope.launch {
            _positionInformation.postValue(value)
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

}