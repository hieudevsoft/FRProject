package com.devapp.fr.data.models.messages

import com.devapp.fr.data.models.MessageType
import java.text.SimpleDateFormat
import java.util.*

abstract class MessageModel(
    open val id: String,
    open val userId: String,
    open var type:MessageType,
    open var isMe:Boolean=true,
    open var react:Int=-1,
    open var isReact:Boolean=false,
    open var time:String = SimpleDateFormat("dd/MM/yyyy, hh:mm aa", Locale.getDefault()).format(Date().time)
    ) {

    abstract fun <T> getContent():T

}