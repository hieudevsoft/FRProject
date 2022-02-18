package com.devapp.fr.data.models.messages

import com.devapp.fr.data.entities.MessageUpload
import com.devapp.fr.data.models.MessageType
import java.text.SimpleDateFormat
import java.util.*

abstract class MessageModel(
    open val id: String="",
    open val userId: String="",
    open var type:MessageType,
    open var isMe:Boolean=true,
    open var react:MutableList<Reaction> = mutableListOf(
        Reaction(-1,0,true),
        Reaction(-1,0,false)
    ),
    open var time:String = SimpleDateFormat("dd/MM/yyyy, hh:mm aa", Locale.getDefault()).format(Date().time)
    ) {
    abstract fun <T> getContent():T
    abstract fun convertToMessageUpload():MessageUpload
}