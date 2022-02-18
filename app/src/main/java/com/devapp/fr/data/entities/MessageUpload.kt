package com.devapp.fr.data.entities

import com.devapp.fr.data.models.MessageType
import com.devapp.fr.data.models.messages.MessageAudio
import com.devapp.fr.data.models.messages.MessageImage
import com.devapp.fr.data.models.messages.MessageModel
import com.devapp.fr.data.models.messages.MessageText

data class MessageUpload(
    val id:String,
    val userId:String="",
    var type: MessageType,
    var isMe:Boolean,
    var message:String,
    var uriImage:String,
    var isPlaying:Boolean,
    var duration:Int,
    var audio:String="",
    var time:String,
    var isActing:Boolean,
){
    constructor():this("","",MessageType.TEXT,false,"","",false,0,"","",false)
    fun convertToMessageModel(type:MessageType):MessageModel{
        return when (type) {
            MessageType.TEXT -> {
                MessageText(id,userId,MessageType.TEXT,isMe,message,isActing)
            }
            MessageType.IMAGE -> {
                MessageImage(id,userId,MessageType.IMAGE,false,uriImage,isActing)
            }
            else -> MessageAudio(id,userId,MessageType.AUDIO,isMe,audio,isPlaying,duration,isActing)
        }
    }
}