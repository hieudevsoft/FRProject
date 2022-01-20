package com.devapp.fr.data.models.messages

import com.devapp.fr.data.models.MessageType

data class MessageAudio(
    override val id:String="",
    override val userId:String="",
    override var type: MessageType,
    override var isMe:Boolean=true,
    private var audio:String?=null,
    var isPlaying:Boolean = false,
    var offsetPlaying:Int? = 0,
    var duration:Int?=null
):MessageModel(id,userId,type) {
    override fun <T> getContent(): T {
        return audio as T;
    }
}