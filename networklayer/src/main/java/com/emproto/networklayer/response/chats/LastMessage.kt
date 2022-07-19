package com.emproto.networklayer.response.chats

data class LastMessage(
    val action: Any,
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