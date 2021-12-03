package com.devapp.fr.data.models.messages

import com.devapp.fr.data.models.MessageType

data class MessageImage(
    override val id:String="",
    override val userId:String="",
    override var type: MessageType,
    override var isMe:Boolean=true,
    var urlImage:String,
):MessageModel(id,userId,type) {
    override fun <T> getContent(): T {
        return urlImage as T;
    }
}