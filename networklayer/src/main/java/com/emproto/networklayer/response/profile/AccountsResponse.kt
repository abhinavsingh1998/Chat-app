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
            @SerializedName("createdAt")
            val createdAt: String,
            @SerializedName("crmBookingId")
            val crmBookingId: String,
            @SerializedName("crmCustomerPaymentId")
            val crmCustomerPaymentId: String,
            @SerializedName("crmLaunchPhaseId")
            val crmLaunchPhaseId: String,
            @SerializedName("documentCategory")
            val documentCategory: String,
            @SerializedName("documentType")
            val documentType: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("status")
            val status: String?=null,
            @SerializedName("itemInternalId")
            val itemInternalId: Any?,
            @SerializedName("name")
            val name: String,
            @SerializedName("path")
            val path: String,
            @SerializedName("updatedAt")
            val updatedAt: String,
            @SerializedName("userId")
            val userId: String
        )

        data class PaymentHistory(
            @SerializedName("createdAt")
            val createdAt: String,
            @SerializedName("crmBookingId")
            val crmBookingId: String,
            @SerializedName("crmId")
            val crmId: String,
            @SerializedName("crmInventory")
            val crmInventory: String?=null,
            @SerializedName("document")
            val document: Document,
            @SerializedName("id")
            val id: Int,
            @SerializedName("launchName")
            val launchName: String,
            @SerializedName("paidAmount")
            val paidAmount: Int,
            @SerializedName("paymentDate")
            val paymentDate: String,
            @SerializedName("updatedAt")
            val updatedAt: String
        ) {
            data class Document(
                @SerializedName("createdAt")
                val createdAt: String,
                @SerializedName("crmBookingId")
                val crmBookingId: String,
                @SerializedName("crmCustomerPaymentId")
                val crmCustomerPaymentId: String,
                @SerializedName("crmLaunchPhaseId")
                val crmLaunchPhaseId: String,
                @SerializedName("documentCategory")
                val documentCategory: String,
                @SerializedName("documentType")
                val documentType: String,
                @SerializedName("id")
                val id: Int,
                @SerializedName("itemInternalId")
                val itemInternalId: Any?,
                @SerializedName("name")
                val name: String,
                @SerializedName("path")
                val path: String,
                @SerializedName("updatedAt")
                val updatedAt: String,
                @SerializedName("userId")
                val userId: String
            )
        }
    }
}