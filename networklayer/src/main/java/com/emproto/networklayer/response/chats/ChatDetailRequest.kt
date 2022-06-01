package com.emproto.networklayer.response.chats

data class ChatDetailRequest(
    val projectId: String,
    val isInvested: Boolean
)