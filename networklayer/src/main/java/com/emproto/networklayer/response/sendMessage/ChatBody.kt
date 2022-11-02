package com.emproto.networklayer.response.sendMessage

data class ChatBody(
    val linkedOption: Int,
    val message: String,
    val options: List<Option>
)