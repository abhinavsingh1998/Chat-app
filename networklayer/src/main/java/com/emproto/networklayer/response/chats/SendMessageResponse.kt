package com.emproto.networklayer.response.chats

data class SendMessageResponse(
    val code: Int,
    val `data`: SData,
    val message: String
)

data class SData(
    val conversation: Conversation,
    val message: SMessage
)

data class Conversation(
    val caseId: Any,
    val createdAt: String,
    val documents: Any,
    val id: Int,
    val messages: List<Any>,
    val option1: Any,
    val option2: Any,
    val projectId: String,
    val smartKey: String,
    val updatedAt: String,
    val userId: Int
)

data class SMessage(
    val action: String,
    val caseId: Any,
    val createdAt: String,
    val documents: Any,
    val id: Int,
    val message: String,
    val origin: String,
    val projectId: String,
    val replyTo: Any,
    val updatedAt: String,
    val userId: Int
)