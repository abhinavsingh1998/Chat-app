package com.emproto.networklayer.request.chat

import com.emproto.networklayer.response.chats.Option

data class SendMessageBody(
    val conversationId: Int,
    val origin: Int,
    val message: String,
    val selection: Int?,
    val actionType: Int?,
    val options: ArrayList<Option>?,
    val replyTo: Int?= null,
    val crmProjectId:String
    )