package com.emproto.networklayer.response.chats

data class ChatInitiateRequest(
    val isInvested:Boolean?,
    val projectId:String="ead5c0e2-b6c7-ec11-a7b5-002248d5b5e1"
)