package com.devapp.fr.data.entities

import com.devapp.fr.data.models.MessageType
import com.devapp.fr.data.models.messages.*

data class MessageUpload(
    var id: String,
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
) {
    constructor() : this(
        "", "", MessageType.TEXT, false, "", "", false, 0, "", "", mutableListOf(
            Reaction(-1, 0, true),
            Reaction(-1, 0, false)
        )
    )

    fun convertToMessageModel(type: MessageType): MessageModel {
        return when (type) {
            MessageType.TEXT -> {
                MessageText(id, userId, MessageType.TEXT, isMe, message).also {
                    it.time = time
                    it.react = react
                }
            }
            MessageType.IMAGE -> {
                MessageImage(id, userId, MessageType.IMAGE, false, uriImage).also {
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
            ).also {
                it.time = time
                it.react = react
            }
        }
    }
}