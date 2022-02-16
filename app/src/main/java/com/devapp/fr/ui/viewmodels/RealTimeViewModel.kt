package com.devapp.fr.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.devapp.fr.data.models.items.Notification
import com.devapp.fr.di.IoDispatcher
import com.devapp.fr.network.RealTimeService
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
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

     fun readNotificationWhenPartnerReply(
        ownerId:String,
        replyCallback:(Notification)->Unit,
        ){
        viewModelScope.launch {
            realTimeService.readNotificationWhenPartnerReply(ownerId, replyCallback)
        }
    }
}