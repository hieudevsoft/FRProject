package com.devapp.fr.data.models.messages

import com.devapp.fr.data.entities.MessageUpload
import com.devapp.fr.data.models.MessageType

data class MessageImage(
    override val id:String="",
    override val userId:String="",
    override var type: MessageType,
    override var isMe:Boolean=true,
    var urlImage:String,
    var isUpLoading:Boolean,
):MessageModel(id,userId,type) {
    override fun <T> getContent(): T {
        return urlImage as T;
    }

    override fun convertToMessageUpload(): MessageUpload {
        return MessageUpload(id,userId,type,isMe,"",urlImage,false,0,"",time,isUpLoading)
    }
}