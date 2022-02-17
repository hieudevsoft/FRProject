package com.devapp.fr.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.provider.SelfDestructiveThread
import com.devapp.fr.data.models.items.Notification
import com.devapp.fr.di.IoDispatcher
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RealTimeService @Inject constructor(private val context:Context) {
    private val refsNotifications = FirebaseDatabase.getInstance().getReference("notifications")
    private val refsChat = FirebaseDatabase.getInstance().getReference("room_chats")
    private val DELAY_RESET = 1000L
    private val TAG = "RealTimeService"

    suspend fun sendNotificationWhenMeReply(
        notification:Notification,
        partnerId:String,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ){
        withContext(dispatcher){
            try {
                refsNotifications.child(partnerId).setValue(notification).await()
                delay(DELAY_RESET)
                refsNotifications.child(partnerId).setValue(null).await()
            }catch (e:Exception){
                Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun readNotificationWhenPartnerReply(
        ownerId:String,
        replyCallback:(Notification)->Unit,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO){
        withContext(dispatcher){
            refsNotifications.child(ownerId).addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue(Notification::class.java)
                    if (value != null) {
                        replyCallback(value)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: ${error.message}")
                }
            })
        }
    }
}