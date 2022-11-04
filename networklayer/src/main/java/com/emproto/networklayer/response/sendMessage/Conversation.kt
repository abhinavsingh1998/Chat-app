package com.emproto.networklayer.response.sendMessage

data class Conversation(
    val caseId: Any,
    val createdAt: String,
    val documents: Any,
    val id: Int,
    val isOpen: Boolean,
    val messages: List<Any>,
    val option1: Any,
    val option2: Any,
    val smartKey: String,
    val topicId: String,
    val updatedAt: String,
    val userId: Int
)