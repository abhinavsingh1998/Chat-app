package com.emproto.hoabl.feature.chat.model.ChatsListModel

import java.io.Serializable

data class ChatsModel(
    val image: Int,
    val topic: String,
    val desc:String,
    val time:String
): Serializable