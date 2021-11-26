package com.devapp.fr.data.models.items

import java.text.SimpleDateFormat
import java.util.*

data class Message(
    val id:String="",
    val userId:String="",
    var message:String,
    var isMe:Boolean=true,
    var react:Int=-1,
    var isTexting:Boolean=false,
    var isReact:Boolean=false,
    var time:String = SimpleDateFormat("dd/MM/yyyy, hh:mm aa", Locale.getDefault()).format(Date().time),
)