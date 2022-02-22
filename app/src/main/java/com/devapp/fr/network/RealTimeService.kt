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
    private val refInformationChat = FirebaseDatabase.getInstance().reference.child("information_chats")
    private val refVideo = FirebaseDatabase.getInstance().reference.child("call_videos")
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

    suspend fun sendNotificationCallVideo(
        partnerId: String,
        roomId: String?,
        nameOwner:String?,
        idOwner:String?,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ):Boolean {
        return withContext(dispatcher) {
            try {
                refVideo.child(partnerId).setValue("$roomId#$nameOwner#$idOwner").await()
                true
            } catch (e: Exception) {
                Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
                false
            }
        }
    }

    suspend fun readNotificationCallVideo(
        ownerId: String,
        replyCallback: (String?) -> Unit,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ) {
        withContext(dispatcher) {
            refVideo.child(ownerId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue(String::class.java)
                    replyCallback(value)

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, "onCancelled: ${error.message}")
                }
            })
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

    suspend fun updateSizeCompareSeenSender(
        senderRoom: String,
        obj: HashMap<String, Any>,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ): Boolean {
        return withContext(dispatcher) {
            try {
                refChats.child(senderRoom).updateChildren(obj).await()
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun updateSizeCompareSeenPartner(
        recieverRoom: String,
        newSize:Int,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) {
        withContext(dispatcher) {
            try {
                refChats.child(recieverRoom).child("new_size").setValue(newSize).await()
            } catch (e: Exception) {

            }
        }
    }

    suspend fun getLastMessage(
        senderRoom: String,
        onSuccessCallBack:(String)->Unit,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) {
         withContext(dispatcher) {
            try {
                refChats.child(senderRoom).child("lastMsg").addValueEventListener(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val lastMsg = snapshot.getValue(String::class.java)
                        refChats.child(senderRoom).child("lastMsgTime").addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val result  = snapshot.getValue(String::class.java)
                                result?.let { onSuccessCallBack("$it#$lastMsg") }
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }

                        })
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })


            } catch (e: Exception) {

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

    suspend fun getCombineOldAndNewSeen(
        senderRoom: String,
        onSuccessCallBack:(String)->Unit,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO,
    ) {
        withContext(dispatcher) {
            try {
                refChats.child(senderRoom).child("new_size").addValueEventListener(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val new_size = snapshot.getValue(Int::class.java)
                        refChats.child(senderRoom).child("old_size").addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val old_size  = snapshot.getValue(Int::class.java)
                                onSuccessCallBack("$new_size#$old_size")
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }

                        })
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })


            } catch (e: Exception) {

            }
        }
    }

    suspend fun setNickNameForPartner(
        nickName: String,
        senderRoom: String,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ):Boolean{
        return withContext(dispatcher) {
            try {
                refInformationChat.child(senderRoom).child("nick_name").setValue(nickName).await()
                true
            } catch (e: Exception) {
                Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
                false
            }
        }
    }

    suspend fun getNickNamePartner(
        senderRoom: String,
        nickNameCallback:(String?)->Unit,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ){
        return withContext(dispatcher) {
            try {
                refInformationChat.child(senderRoom).child("nick_name").addValueEventListener(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val value = snapshot.getValue(String::class.java)
                        nickNameCallback(value)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        nickNameCallback(null)
                    }

                })
            } catch (e: Exception) {
                Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()

            }
        }
    }

    suspend fun setColorBoxChat(
        color:String,
        senderRoom: String,
        receiverRoom: String,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ):Boolean{
        return withContext(dispatcher) {
            try {
                refInformationChat.child(senderRoom).child("color_box").setValue(color).await()
                refInformationChat.child(receiverRoom).child("color_box").setValue(color).await()
                true
            } catch (e: Exception) {
                Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
                false
            }
        }
    }

    suspend fun getColorBoxChat(
        senderRoom: String,
        colorBoxCallback:(String?)->Unit,
        @IoDispatcher dispatcher: CoroutineDispatcher = Dispatchers.IO
    ){
        return withContext(dispatcher) {
            try {
                refInformationChat.child(senderRoom).child("color_box").addValueEventListener(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val value = snapshot.getValue(String::class.java)
                        colorBoxCallback(value)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        colorBoxCallback(null)
                    }

                })
            } catch (e: Exception) {
                Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()

            }
        }
    }

}