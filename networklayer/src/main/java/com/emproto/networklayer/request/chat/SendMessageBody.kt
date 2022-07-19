package com.emproto.networklayer.request.chat

data class SendMessageBody(
    val conversationId: String,
    val document: String = "",
    val message: String,
    val origin: String = "customer",
    val projectId: String,
    val replyTo: Int = 1,
    val smartKey: String
)