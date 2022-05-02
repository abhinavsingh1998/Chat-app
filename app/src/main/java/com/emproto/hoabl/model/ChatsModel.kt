package com.emproto.hoabl.model

import java.io.Serializable

data class ChatsModel(
    val image: Int,
    val topic: String,
    val desc:String,
    val time:String
): Serializable