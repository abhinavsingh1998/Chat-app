package com.emproto.networklayer.response.chats


data class ChatHistoryResponse(
    val code: Int,
    val `data`: Data,
    val message: String
)

data class Data(
    val autoChat: AutoChat,
    val conversation: CConversation,
    val messages: List<Message>
)

data class AutoChat(
    val chatJSON: ChatJSON,
    val createdAt: String,
    val id: Int,
    val smartKey: String,
    val updatedAt: String
)

data class CConversation(
    val caseId: Any,
    val createdAt: String,
    val documents: Any,
    val id: Int,
    val isOpen: Boolean,
    val messages: List<Any>,
    val option1: String,
    val option2: String,
    val smartKey: String,
    val topicId: String,
    val updatedAt: String,
    val userId: Int
)

data class Message(
    val action: Int,
    val conversationId: Int,
    val createdAt: String,
    val crmId: Any,
    val documents: Any,
    val id: Int,
    val message: String,
    val options: ArrayList<Option>,
    val origin: Int,
    val replyTo: Int,
    val updatedAt: String
)

data class ChatJSON(
    val allowTypingMessage: String,
    val chatBody: List<ChatBody>,
    val finalMessage: String,
    val inactiveMessage: String,
    val welcomeMessage: String
)

data class CChatBody(
    val linkedOption: Int,
    val message: String,
    val options: ArrayList<Option>
)

data class COption(
    val action: Int,
    val actionType: Int,
    val optionNumber: Int,
    val text: String
)