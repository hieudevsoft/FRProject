package com.devapp.fr.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
    private val realTimeService: RealTimeService,
    @IoDispatcher private val defaultDispatcher:CoroutineDispatcher = Dispatchers.IO
):AndroidViewModel(app) {

    fun sendNotificationWhenMeReply(
        notification: Notification?,
        partnerId:String,
    ){
        viewModelScope.launch(defaultDispatcher) {
            realTimeService.sendNotificationWhenMeReply(notification,partnerId)
        }
    }

    fun sendStatusOnOff(
        account: AccountOnline,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ){
        viewModelScope.launch(defaultDispatcher){
            realTimeService.sendStatusOnOff(account)
        }
    }

     fun readNotificationWhenPartnerReply(
        ownerId:String,
        replyCallback:(Notification?)->Unit,
        ){
        viewModelScope.launch(defaultDispatcher){
            realTimeService.readNotificationWhenPartnerReply(ownerId, replyCallback)
        }
    }

    fun getListOnlineApp(
        ownerId:String,
        replyCallback:(HashMap<String,Boolean>)->Unit,
    ){
        viewModelScope.launch(defaultDispatcher){
            realTimeService.getListOnlineApp(ownerId, replyCallback)
        }
    }

    private val _stateFlowUserOnOff:MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val stateFlowUserOnOff:StateFlow<Boolean?> =_stateFlowUserOnOff
    fun checkUserOnOffbyId(
        id:String,
       ){
        viewModelScope.launch(defaultDispatcher){
        realTimeService.checkUserOnOffbyId(id,defaultDispatcher){
                _stateFlowUserOnOff.value = it
            }
        }
    }

    fun checkUserOnOffbyId(
        id:String,
        callBack:(Boolean)->Unit,
       ){
        viewModelScope.launch(defaultDispatcher) {
            realTimeService.checkUserOnOffbyId(id,defaultDispatcher){
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
        ){
        viewModelScope.launch(defaultDispatcher) {
            _stateFlowUpdateLastMessage.value = realTimeService.updateLastMessage(senderRoom,recieverRoom,lastObj)
        }
    }

    private val _stateFlowUpdateMessage:MutableStateFlow<Boolean?> = MutableStateFlow(null)
    fun resetStateFlowUpdateMessage() {
        _stateFlowUpdateMessage.value = null
    }
    val stateUpdateMessage:StateFlow<Boolean?> =_stateFlowUpdateMessage
    fun updateMessage(
        senderRoom:String,
        recieverRoom:String,
        message:MessageModel,
    ){
        viewModelScope.launch(defaultDispatcher) {
            _stateFlowUpdateMessage.value = realTimeService.updateMessage(senderRoom,recieverRoom,message)
        }
    }

    private val _stateFlowUpdateActing:MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val stateUpdateActing:StateFlow<Boolean?> =_stateFlowUpdateActing
    fun updateActing(
        recieverRoom:String,
        acting:String,
        ){
        viewModelScope.launch(defaultDispatcher){
            _stateFlowUpdateActing.value = realTimeService.updateActing(recieverRoom,acting)
        }
    }

    private val _stateFlowReadActing:MutableStateFlow<String?> = MutableStateFlow(null)
    val stateReadActing:StateFlow<String?> =_stateFlowReadActing
    fun readActing(
        senderRoom:String,
      ){
        viewModelScope.launch(defaultDispatcher){
            realTimeService.readActing(senderRoom,{_stateFlowReadActing.value = it},{_stateFlowReadActing.value = null})
        }
    }

    private val _stateFlowSendMessageToFirebase:MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val stateSendMessageToFirebase:StateFlow<Boolean?> =_stateFlowSendMessageToFirebase
    fun resetStateSendMessageToFirebase() {
        _stateFlowSendMessageToFirebase.value = null
    }
    fun sendMessageToFirebase(senderRoom: String,recieverRoom: String,data:MessageUpload){
        viewModelScope.launch(defaultDispatcher){
            _stateFlowSendMessageToFirebase.value = realTimeService.sendMessageToFirebase(senderRoom,recieverRoom,data)

        }
    }

    private val _stateFlowGetListMessage:MutableStateFlow<List<MessageModel>?> = MutableStateFlow(emptyList())
    val stateGetListMessage:StateFlow<List<MessageModel>?> =_stateFlowGetListMessage
    fun getListMessage(senderRoom: String){
        viewModelScope.launch(defaultDispatcher){
            realTimeService.getListMessage(senderRoom,{
                _stateFlowGetListMessage.value = it.toList()
            },{
                _stateFlowGetListMessage.value = null
            })
        }
    }
}
