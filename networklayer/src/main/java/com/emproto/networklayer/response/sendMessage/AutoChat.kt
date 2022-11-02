package com.emproto.networklayer.response.sendMessage

data class AutoChat(
    val chatJSON: ChatJSON,
    val createdAt: String,
    val id: Int,
    val smartKey: String,
    val updatedAt: String
)