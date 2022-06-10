package com.emproto.networklayer.response.profile


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AccountsResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String
) {
    data class Data(
        @SerializedName("documents")
        val documents: List<Document>,
        @SerializedName("paymentHistory")
        val paymentHistory: List<PaymentHistory>
    ) {
        data class Document(
            @SerializedName("documentCategory")
            val documentCategory: String,
            @SerializedName("documentType")
            val documentType: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("link")
            val link: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("projectId")
            val projectId: String?,
            @SerializedName("userId")
            val userId: Int
        )

        data class PaymentHistory(
            @SerializedName("bookingId")
            val bookingId: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("paidAmount")
            val paidAmount: Int,
            @SerializedName("paymentDate")
            val paymentDate: String,
            @SerializedName("paymentMilestone")
            val paymentMilestone: String,
            @SerializedName("projectName")
            val projectName: String,
            @SerializedName("receiptLink")
            val receiptLink: Any?,
            @SerializedName("userId")
            val userId: Int
        ) : Serializable
    }
}