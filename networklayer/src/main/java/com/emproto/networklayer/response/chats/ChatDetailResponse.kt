package com.emproto.networklayer.response.chats


import com.google.gson.annotations.SerializedName

data class ChatDetailResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val chatDetailList: ChatDetailList,
    @SerializedName("message")
    val message: String
) {

    data class ChatDetailList(
        @SerializedName("autoChat")
        val autoChat: AutoChat,

        @SerializedName("conversation")
        val conversation: Conversation
        ) {
        data class AutoChat(

            @SerializedName("chatJSON")
            val chatJSON: ChatJSON,

            @SerializedName("id")
            val id: Int,
            @SerializedName("smartKey")
            val smartKey: String,

            @SerializedName("createdAt")
            val createdAt: String,
            @SerializedName("updatedAt")
            val updatedAt: String
        ) {
            data class ChatJSON(
                @SerializedName("allowTypingMessage")
                val allowTypingMessage: String,
                @SerializedName("finalMessage")
                val finalMessage: String,
                @SerializedName("welcomeMessage")
                val welcomeMessage: String,
                @SerializedName("chatBody")
                val chatBody: List<ChatBody>
            )
        }

        data class Conversation(
            @SerializedName("caseId")
            val caseId: Any?,
            @SerializedName("createdAt")
            val createdAt: String,
            @SerializedName("documents")
            val documents: Any?,
            @SerializedName("id")
            val id: Int,
            @SerializedName("messages")
            val messages: List<Any>,
            @SerializedName("option1")
            val option1: Any?,
            @SerializedName("option2")
            val option2: Any?,
            @SerializedName("projectId")
            val projectId: String,
            @SerializedName("smartKey")
            val smartKey: String,
            @SerializedName("updatedAt")
            val updatedAt: String,
            @SerializedName("userId")
            val userId: Int
        )
    }
}

data class ChatBody(
    @SerializedName("linkedOption")
    val linkedOption: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("options")
    val options: ArrayList<Option>?
)

data class Option(
    @SerializedName("action")
    val action: String?,
    @SerializedName("actionType")
    val actionType: Int,
    @SerializedName("optionNumber")
    val optionNumber: Int?,
    @SerializedName("text")
    val text: String?
)