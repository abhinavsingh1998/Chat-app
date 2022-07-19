package com.emproto.networklayer.response.chats

data class ChatHistoryResponse(
    val code: Int,
    val `data`: Data,
    val message: String
)

data class Data(
    val messages: List<Message>
)

data class Message(
    val action: Any,
    val caseId: Any,
    val documents: Any,
    val id: Int,
    val message: String,
    val origin: String,
    val projectId: String,
    val replyTo: Any,
    val userId: Int
)