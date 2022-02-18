package com.devapp.fr.data.entities

import com.devapp.fr.data.models.MessageType
import com.devapp.fr.data.models.messages.*

data class MessageUpload(
    val id: String,
    val userId: String = "",
    var type: MessageType,
    var isMe: Boolean,
    var message: String,
    var uriImage: String,
    var isPlaying: Boolean,
    var duration: Int,
    var audio: String = "",
    var time: String,
    val react: MutableList<Reaction>,
    var isActing: Boolean,
) {
    constructor() : this(
        "", "", MessageType.TEXT, false, "", "", false, 0, "", "", mutableListOf(
            Reaction(-1, 0, true),
            Reaction(-1, 0, false)
        ), false
    )

    fun convertToMessageModel(type: MessageType): MessageModel {
        return when (type) {
            MessageType.TEXT -> {
                MessageText(id, userId, MessageType.TEXT, isMe, message,isActing).also {
                    it.time = time
                    it.react = react
                }
            }
            MessageType.IMAGE -> {
                MessageImage(id, userId, MessageType.IMAGE, false, uriImage, isActing).also {
                    it.time = time
                    it.react = react
                }
            }
            else -> MessageAudio(
                id,
                userId,
                MessageType.AUDIO,
                isMe,
                audio,
                isPlaying,
                duration,
                isActing
            ).also {
                it.time = time
                it.react = react
            }
        }
    }
}