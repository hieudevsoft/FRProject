package com.devapp.fr.ui.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.fr.data.models.items.AccountOnline
import com.devapp.fr.data.models.items.Notification
import com.devapp.fr.di.IoDispatcher
import com.devapp.fr.network.RealTimeService
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class RealTimeViewModel @Inject constructor(
    private val app:Application,
    private val realTimeService: RealTimeService
):AndroidViewModel(app) {

    fun sendNotificationWhenMeReply(
        notification: Notification,
        partnerId:String,
    ){
        viewModelScope.launch {
            realTimeService.sendNotificationWhenMeReply(notification,partnerId)
        }
    }

    fun sendStatusOnOff(
        account: AccountOnline,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ){
        viewModelScope.launch {
            realTimeService.sendStatusOnOff(account)
        }
    }

     fun readNotificationWhenPartnerReply(
        ownerId:String,
        replyCallback:(Notification)->Unit,
        ){
        viewModelScope.launch {
            realTimeService.readNotificationWhenPartnerReply(ownerId, replyCallback)
        }
    }

    fun getListOnlineApp(
        ownerId:String,
        replyCallback:(HashMap<String,Boolean>)->Unit,
    ){
        viewModelScope.launch {
            realTimeService.getListOnlineApp(ownerId, replyCallback)
        }
    }

    private val _stateFlowUserOnOff:MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val stateFlowUserOnOff:StateFlow<Boolean?> =_stateFlowUserOnOff
    fun checkUserOnOffbyId(
        id:String,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO){
        viewModelScope.launch {
        realTimeService.checkUserOnOffbyId(id,dispatcher){
                _stateFlowUserOnOff.value = it
            }
        }
    }

    fun checkUserOnOffbyId(
        id:String,
        callBack:(Boolean)->Unit,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO){
        viewModelScope.launch {
            realTimeService.checkUserOnOffbyId(id,dispatcher){
                callBack(it)
            }
        }
    }

    private val _stateFlowUpdateLastMessage:MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val stateUpdateLastMessage:StateFlow<Boolean?> =_stateFlowUpdateLastMessage
    fun updateLastMessage(
        senderRoom:String,
        recieverRoom:String,
        lastObj:HashMap<String,Any>,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO){
        viewModelScope.launch {
            _stateFlowUpdateLastMessage.value = realTimeService.updateLastMessage(senderRoom,recieverRoom,lastObj)
        }
    }
}