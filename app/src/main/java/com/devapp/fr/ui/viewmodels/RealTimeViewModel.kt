package com.devapp.fr.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.fr.data.entities.MessageUpload
import com.devapp.fr.data.models.items.AccountOnline
import com.devapp.fr.data.models.items.Notification
import com.devapp.fr.data.models.messages.MessageModel
import com.devapp.fr.di.IoDispatcher
import com.devapp.fr.network.RealTimeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RealTimeViewModel @Inject constructor(
    private val app:Application,
    private val realTimeService: RealTimeService
):AndroidViewModel(app) {

    fun sendNotificationWhenMeReply(
        notification: Notification?,
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
        replyCallback:(Notification?)->Unit,
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

    private val _stateFlowUpdateActing:MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val stateUpdateActing:StateFlow<Boolean?> =_stateFlowUpdateActing
    fun updateActing(
        recieverRoom:String,
        acting:String,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO){
        viewModelScope.launch {
            _stateFlowUpdateActing.value = realTimeService.updateActing(recieverRoom,acting)
        }
    }

    private val _stateFlowReadActing:MutableStateFlow<String?> = MutableStateFlow(null)
    val stateReadActing:StateFlow<String?> =_stateFlowReadActing
    fun readActing(
        senderRoom:String,
      ){
        viewModelScope.launch {
            realTimeService.readActing(senderRoom,{_stateFlowReadActing.value = it},{_stateFlowReadActing.value = null})
        }
    }

    private val _stateFlowSendMessageToFirebase:MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val stateSendMessageToFirebase:StateFlow<Boolean?> =_stateFlowSendMessageToFirebase
    fun sendMessageToFirebase(senderRoom: String,recieverRoom: String,data:MessageUpload){
        viewModelScope.launch {
            _stateFlowSendMessageToFirebase.value = realTimeService.sendMessageToFirebase(senderRoom,recieverRoom,data)
            _stateFlowSendMessageToFirebase.value = null
        }
    }

    private val _stateFlowGetListMessage:MutableStateFlow<List<MessageModel>?> = MutableStateFlow(emptyList())
    val stateGetListMessage:StateFlow<List<MessageModel>?> =_stateFlowGetListMessage
    fun getListMessage(senderRoom: String){
        viewModelScope.launch {
            realTimeService.getListMessage(senderRoom,{
                _stateFlowGetListMessage.value = it.toList()
            },{
                _stateFlowGetListMessage.value = null
            })
        }
    }
}