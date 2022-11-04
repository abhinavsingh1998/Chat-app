package com.emproto.networklayer.response.chats

data class ChatInitiateRequest(
    val isInvested:Boolean?,
    val topicId:String
)