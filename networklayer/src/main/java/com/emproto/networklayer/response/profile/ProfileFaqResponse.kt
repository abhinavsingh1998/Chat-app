package com.emproto.networklayer.response.profile


import com.google.gson.annotations.SerializedName

data class ProfileFaqResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: ArrayList<ProfileFaqData>,
    @SerializedName("message")
    val message: String
) {
    data class ProfileFaqData(
        @SerializedName("categoryId")
        val categoryId: Int,
        @SerializedName("createdAt")
        val createdAt: String,
        @SerializedName("createdBy")
        val createdBy: Int,
        @SerializedName("faqAnswer")
        val faqAnswer: FaqAnswer,
        @SerializedName("faqQuestion")
        val faqQuestion: FaqQuestion,
        @SerializedName("id")
        val id: Int,
        @SerializedName("priority")
        val priority: Int,
        @SerializedName("status")
        val status: Boolean,
        @SerializedName("typeOfFAQ")
        val typeOfFAQ: String,
        @SerializedName("updatedAt")
        val updatedAt: String,
        @SerializedName("updatedBy")
        val updatedBy: Any?
    ) {
        data class FaqAnswer(
            @SerializedName("answer")
            val answer: String
        )

        data class FaqQuestion(
            @SerializedName("question")
            val question: String
        )
    }
}