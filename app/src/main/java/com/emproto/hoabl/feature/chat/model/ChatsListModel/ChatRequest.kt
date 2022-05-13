package com.emproto.hoabl.feature.chat.model.ChatsListModel

import java.io.Serializable

data class ChatRequest(
    val image: Int,
    val topic: String,
    val desc:String,
    val time:String
): Serializable