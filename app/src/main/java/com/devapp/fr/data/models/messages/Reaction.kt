package com.devapp.fr.data.models.messages

data class Reaction(var react:Int,var count:Int,var isMe:Boolean) {
    constructor():this(-1,0,false)
}