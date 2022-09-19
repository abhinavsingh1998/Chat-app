package com.emproto.hoabl.feature.chat.model

import com.emproto.networklayer.response.chats.Option

data class ChatDetailModel(
    val message: String? = null,
    val option: ArrayList<Option>? = null,
    val messageType: MessageType,
    val time: String?,
    val conversationId:Int=0
)
enum class MessageType {
    SENDER, RECEIVER
}
