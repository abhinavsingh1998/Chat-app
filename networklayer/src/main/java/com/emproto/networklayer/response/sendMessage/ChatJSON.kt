package com.emproto.networklayer.response.sendMessage

data class ChatJSON(
    val allowTypingMessage: String,
    val chatBody: List<ChatBody>,
    val finalMessage: String,
    val inactiveMessage: String,
    val welcomeMessage: String
)