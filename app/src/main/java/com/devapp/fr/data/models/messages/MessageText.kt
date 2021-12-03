package com.devapp.fr.data.models.messages

import com.devapp.fr.data.models.MessageType
import java.text.SimpleDateFormat
import java.util.*

data class MessageText(
    override val id:String="",
    override val userId:String="",
    override var type:MessageType,
    override var isMe:Boolean=true,
    var message:String,
    var isTexting:Boolean=false,
):MessageModel(id,userId,type) {
    override fun <T> getContent(): T {
        return message as T;
    }
}
