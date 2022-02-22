package com.devapp.fr.data.models.messages

import com.devapp.fr.data.entities.MessageUpload
import com.devapp.fr.data.models.MessageType
import java.text.SimpleDateFormat
import java.util.*

data class MessageText(
    override val id:String="",
    override val userId:String="",
    override var type:MessageType,
    override var isMe:Boolean=true,
    var message:String,
):MessageModel(id,userId,type) {
    override fun <T> getContent(): T {
        return message as T;
    }

    override fun convertToMessageUpload(): MessageUpload {
        return MessageUpload(id,userId,type,isMe,message,"",false,0,"",time,react)
    }
}
