package com.emproto.networklayer.response.chats
import com.google.gson.annotations.SerializedName

data class ChatDetailResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: ChatDetailList,
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
            @SerializedName("createdAt")
            val createdAt: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("smartKey")
            val smartKey: String,
            @SerializedName("updatedAt")
            val updatedAt: String
        ) {
            data class ChatJSON(
                @SerializedName("allowTypingMessage")
                val allowTypingMessage: String,
                @SerializedName("chatBody")
                val chatBody: ChatBody,
                @SerializedName("finalMessage")
                val finalMessage: String,
                @SerializedName("welcomeMessage")
                val welcomeMessage: String
            ) {
                data class ChatBody(
                    @SerializedName("message")
                    val message: String,
                    @SerializedName("options")
                    val options: List<Option>
                ) {
                    data class Option(
                        @SerializedName("action")
                        val action: String,
                        @SerializedName("message")
                        val message: String,
                        @SerializedName("optionNumber")
                        val optionNumber: Int,
                        @SerializedName("options")
                        val options: List<Option>,
                        @SerializedName("text")
                        val text: String
                    ) {
                        data class Option(
                            @SerializedName("Action")
                            val action: String,
                            @SerializedName("optionNumber")
                            val optionNumber: Int,
                            @SerializedName("text")
                            val text: String
                        )
                    }
                }
            }
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