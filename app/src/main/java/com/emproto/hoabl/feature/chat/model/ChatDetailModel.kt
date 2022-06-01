package com.emproto.hoabl.feature.chat.model

import com.emproto.networklayer.response.chats.ChatDetailResponse
import com.emproto.networklayer.response.chats.Option

data class ChatDetailModel(
    val message: String? = null,
    val option: ArrayList<Option>? = null,
    val messageType: MessageType,
    val time: String?
)


enum class MessageType {
    SENDER, RECEIVER
}

enum class ActionType {
    NAVIGATE,
    MORE_OPTIONS,
    ALLOW_TYPING,
    NOT_ALLOWED_TYPING


}

enum class Action {
    NAVIGATE_ABOUT_HOABL,
    NAVIGATE_PROMISES


}