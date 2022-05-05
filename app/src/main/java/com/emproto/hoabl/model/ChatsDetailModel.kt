package com.emproto.hoabl.model


import java.io.Serializable

data class ChatsDetailModel(
    val msgReceived: ArrayList<MessageReceived>? = null,
    val msgOptionsList: ArrayList<MessageOptions>? = null,
    val msgReply: String? = null,
    val typeOfMessage: TypeOfMessage,
    val msgTime:Long
) : Serializable

data class MessageReceived(
    val msgId: String,
    val msgReceived: String
) : Serializable

data class MessageOptions(
    val optionId: String,
    val optionName: String
) : Serializable

enum class TypeOfMessage {
    SENDER,
    RECEIVER
}