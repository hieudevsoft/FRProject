package com.devapp.fr.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.provider.SelfDestructiveThread
import com.devapp.fr.data.entities.MessageUpload
import com.devapp.fr.data.models.items.AccountOnline
import com.devapp.fr.data.models.items.Notification
import com.devapp.fr.data.models.messages.MessageModel
import com.devapp.fr.di.IoDispatcher
import com.devapp.fr.util.UiHelper.hideKeyboard
import com.devapp.fr.util.UiHelper.showSnackbar
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.google.firebase.database.GenericTypeIndicator
import java.io.StreamCorruptedException


class RealTimeService @Inject constructor(private val context: Context) {
    private val refsNotifications = FirebaseDatabase.getInstance().getReference("notifications")
    private val refsOnline = FirebaseDatabase.getInstance().getReference("online")
    private val refChats = FirebaseDatabase.getInstance().reference.child("chats")
    private val DELAY_RESET = 1000L
    private val TAG = "RealTimeService"

    suspend fun sendNotificationWhenMeReply(
        notification: Notification?,
        partnerId: String,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ) {
        withContext(dispatcher) {
            try {
                refsNotifications.child(partnerId).setValue(notification).await()
            } catch (e: Exception) {
                Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun sendStatusOnOff(
        account: AccountOnline,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ) {
        withContext(dispatcher) {
            try {
                refsOnline.child(account.id).setValue(account.isOnline).await()
            } catch (e: Exception) {
                Log.d(TAG, "sendStatusOnOff: ${e.message}")
            }
        }
    }

    suspend fun readNotificationWhenPartnerReply(
        ownerId: String,
        replyCallback: (Notification) -> Unit,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ) {
        withContext(dispatcher) {
            refsNotifications.child(ownerId).addValueEventListener(object : ValueEventListener {
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
        ownerId: String,
        replyCallback: (HashMap<String, Boolean>) -> Unit,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ) {
        withContext(dispatcher) {
            refsOnline.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val t: GenericTypeIndicator<HashMap<String, Boolean>> =
                        object : GenericTypeIndicator<HashMap<String, Boolean>>() {}
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
        id: String,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO,
        replyCallback: (Boolean) -> Unit,
    ) {
        withContext(dispatcher) {
            refsOnline.child(id).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.getValue(Boolean::class.java)?.let { replyCallback(it) }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }

    suspend fun updateLastMessage(
        senderRoom: String,
        reciverRoom: String,
        lastObj: HashMap<String, Any>,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ): Boolean {
        return withContext(dispatcher) {
            try {
                refChats.child(senderRoom).updateChildren(lastObj).await()
                refChats.child(reciverRoom).updateChildren(lastObj).await()
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun updateMessage(
        senderRoom: String,
        reciverRoom: String,
        message: MessageModel,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ): Boolean {
        return withContext(dispatcher) {
            try {
                refChats.child(senderRoom).child("conversation").child(message.id).setValue(message.convertToMessageUpload()).await()
                refChats.child(reciverRoom).child("conversation").child(message.id).setValue(message.convertToMessageUpload()).await()
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun sendMessageToFirebase(
        senderRoomId: String, recieverRoomId: String, data: MessageUpload,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ):Boolean{
       return withContext(dispatcher){
            try {
                val randomKey = FirebaseDatabase.getInstance().reference.push().key!!
                data.id = randomKey
                refChats.child(senderRoomId).child("conversation").child(randomKey).setValue(data).await()
                refChats.child(recieverRoomId).child("conversation").child(randomKey).setValue(data).await()
                true
            }catch (e:Exception){
                Log.d(TAG, "sendMessage: ${e.message}")
                false
            }
        }
    }

    suspend fun getListMessage(
        senderRoomId: String,
        onDataChangeCallback:(List<MessageModel>)->Unit,
        onFailureCallback:()->Unit,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ){
        withContext(dispatcher){
            try {
                refChats.child(senderRoomId).child("conversation").addValueEventListener(object : ValueEventListener {
                    val listMessage:MutableList<MessageModel> = mutableListOf()
                    override fun onDataChange(snapshot: DataSnapshot) {
                        listMessage.clear()
                        if(snapshot.children.count()==0) onDataChangeCallback(listMessage)
                        else snapshot.children.forEach {
                            val message: MessageUpload? = it.getValue(MessageUpload::class.java)
                            if (message != null) {
                                listMessage.add(message.convertToMessageModel(message.type))
                                onDataChangeCallback(listMessage)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        onFailureCallback()
                    }

                })

            }catch (e:Exception){
                Log.d(TAG, "sendMessage: ${e.message}")
            }
        }
    }

    suspend fun updateActing(
        reciverRoom: String,
        acting:String,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ): Boolean {
        return withContext(dispatcher) {
            try {
                refChats.child(reciverRoom).child("acting").setValue(acting).await()
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun readActing(
        senderRoomId: String,
        onDataChangeCallback: (String) -> Unit,
        onFailureCallback: () -> Unit,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) {
         withContext(dispatcher) {
            try {
                refChats.child(senderRoomId).child("acting").addValueEventListener(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.getValue(String::class.java)?.let { onDataChangeCallback(it) }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        onFailureCallback()
                    }

                })

            } catch (e: Exception) {

            }
        }
    }
}