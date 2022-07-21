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
    val crmTicketId: Any,
    val documents: Any,
    val id: Int,
    val isOpen: Boolean,
    val messages: List<Any>,
    val option1: Any,
    val option2: Any,
    val projectId: String,
    val smartKey: String,
    val updatedAt: String,
    val userId: Int
)

data class Message(
    val conversationId: Int,
    val documents: Any,
    val id: Int,
    val message: String,
    val options: ArrayList<Option>,
    val origin: String,
    val projectId: String,
    val replyTo: Any,
    val userId: Int
)

data class ChatJSON(
    val allowTypingMessage: String,
    val chatBody: List<CChatBody>,
    val finalMessage: String,
    val welcomeMessage: String
)

data class CChatBody(
    val linkedOption: Int,
    val message: String,
    val options: ArrayList<Option>
)

data class COption(
    val action: Int,
    val actionType: String,
    val optionNumber: Int,
    val text: String
)