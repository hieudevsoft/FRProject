package com.devapp.fr.data.models.messages

import com.devapp.fr.data.models.MessageType

data class MessageAudio(
    override val id:String="",
    override val userId:String="",
    override var type: MessageType,
    override var isMe:Boolean=true,
    var audio:String,
):MessageModel(id,userId,type) {
    override fun <T> getContent(): T {
        return audio as T;
    }
}