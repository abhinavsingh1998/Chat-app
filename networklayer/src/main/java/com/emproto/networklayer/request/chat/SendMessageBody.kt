package com.emproto.networklayer.request.chat

import com.emproto.networklayer.response.chats.Option

data class SendMessageBody(
    val conversationId: String,
    val document: String = "",
    val message: String,
    val origin: Int ,
    val selection : Int?,
    val replyTo: Int = 1,
    val crmLaunchPhaseId : String,
    val launchPhaseId:String,
    val crmProjectId:String,
    val options: ArrayList<Option>?
)