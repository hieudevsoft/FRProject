package com.devapp.fr.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.provider.SelfDestructiveThread
import com.devapp.fr.data.models.items.AccountOnline
import com.devapp.fr.data.models.items.Notification
import com.devapp.fr.di.IoDispatcher
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.google.firebase.database.GenericTypeIndicator
import java.io.StreamCorruptedException


class RealTimeService @Inject constructor(private val context:Context) {
    private val refsNotifications = FirebaseDatabase.getInstance().getReference("notifications")
    private val refsChat = FirebaseDatabase.getInstance().getReference("room_chats")
    private val refsOnline = FirebaseDatabase.getInstance().getReference("online")
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

    suspend fun sendStatusOnOff(
        account:AccountOnline,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ){
        withContext(dispatcher){
            try {
                refsOnline.child(account.id).setValue(account.isOnline).await()
            }catch (e:Exception){
                Log.d(TAG, "sendStatusOnOff: ${e.message}")
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

    suspend fun getListOnlineApp(
        ownerId:String,
        replyCallback:(HashMap<String,Boolean>)->Unit,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO){
        withContext(dispatcher){
            refsOnline.addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val t: GenericTypeIndicator<HashMap<String,Boolean>> = object : GenericTypeIndicator<HashMap<String,Boolean>>() {}
                    val value = snapshot.getValue(t)
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

    suspend fun checkUserOnOffbyId(
        id:String,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO,
        replyCallback:(Boolean)->Unit,
    ) {
        withContext(dispatcher){
            refsOnline.child(id).addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        snapshot.getValue(Boolean::class.java)?.let { replyCallback(it) }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }
}