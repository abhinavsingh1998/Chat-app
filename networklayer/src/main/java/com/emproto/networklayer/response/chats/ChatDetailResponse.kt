package com.emproto.networklayer.response.chats


import com.google.gson.annotations.SerializedName

data class ChatDetailResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("conversation")
    val conversation: Conversation,
    @SerializedName("data")
    val chatDetailList: ChatDetailList,
    @SerializedName("message")
    val message: String
) {
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

    data class ChatDetailList(
        @SerializedName("autoChat")
        val autoChat: AutoChat,
        @SerializedName("createdAt")
        val createdAt: String,
        @SerializedName("updatedAt")
        val updatedAt: String
    ) {
        data class AutoChat(
            @SerializedName("allowTypingMessage")
            val allowTypingMessage: String,
            @SerializedName("chatJSON")
            val chatJSON: ChatJSON,
            @SerializedName("finalMessage")
            val finalMessage: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("smartKey")
            val smartKey: String,
            @SerializedName("welcomeMessage")
            val welcomeMessage: String
        ) {
            data class ChatJSON(
                @SerializedName("chatBody")
                val chatBody: List<ChatBody>
            )
        }
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
    val actionType: String?,
    @SerializedName("optionNumber")
    val optionNumber: Int?,
    @SerializedName("text")
    val text: String?
)