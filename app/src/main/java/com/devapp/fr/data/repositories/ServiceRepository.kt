package com.devapp.fr.data.repositories

import com.devapp.fr.data.models.items.Notification
import com.devapp.fr.di.IoDispatcher
import com.devapp.fr.network.RealTimeService
import com.devapp.fr.ui.viewmodels.RealTimeViewModel
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ServiceRepository @Inject constructor(private val realTimeService: RealTimeService){
    suspend fun readNotification(ownerId:String,callBack:(Notification?)->Unit,@IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO){
        withContext(dispatcher){
            realTimeService.readNotificationWhenPartnerReply(ownerId,callBack)
            realTimeService.sendNotificationWhenMeReply(null,ownerId)
        }
    }
}