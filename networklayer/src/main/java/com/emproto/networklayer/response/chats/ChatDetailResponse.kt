package com.emproto.networklayer.response.chats


import java.io.Serializable

data class ChatDetailResponse(
    val code: Int,
    val `data`: DData,
    val message: String
):Serializable

data class DData(
    val autoChat: CAutoChat,
    val conversation: DConversation
):Serializable

data class CAutoChat(
    val chatJSON: DChatJSON,
    val createdAt: String,
    val id: Int,
    val smartKey: String,
    val updatedAt: String
):Serializable

data class DConversation(
    val caseId: Any,
    val createdAt: String,
    val documents: Any,
    val id: Int,
    val isOpen: Boolean,
    val messages: List<String>,
    val option1: Any,
    val option2: Any,
    val smartKey: String,
    val topicId: String,
    val updatedAt: String,
    val userId: Int
):Serializable

data class DChatJSON(
    val allowTypingMessage: String,
    val chatBody: List<ChatBody>,
    val finalMessage: String,
    val inactiveMessage: String,
    val welcomeMessage: String
):Serializable

data class ChatBody(
    val linkedOption: Int,
    val message: String,
    val options: ArrayList<Option>?
):Serializable

data class Option(
    val action: Int,
    val actionType: Int?,
    val optionNumber: Int,
    val text: String
):Serializable