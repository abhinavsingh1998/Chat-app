package com.emproto.networklayer.response.profile


import com.google.gson.annotations.SerializedName

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
            @SerializedName("bookingId")
            val bookingId: Any?,
            @SerializedName("documentCategory")
            val documentCategory: Any?,
            @SerializedName("documentType")
            val documentType: Any?,
            @SerializedName("id")
            val id: Int,
            @SerializedName("itemInternalId")
            val itemInternalId: Any?,
            @SerializedName("name")
            val name: String,
            @SerializedName("path")
            val path: Any?,
            @SerializedName("paymentId")
            val paymentId: Any?,
            @SerializedName("projectId")
            val projectId: String?,
            @SerializedName("userId")
            val userId: Any?
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
            @SerializedName("projectName")
            val projectName: String,
            @SerializedName("receiptLink")
            val receiptLink: String,
            @SerializedName("userId")
            val userId: String
        )
    }
}