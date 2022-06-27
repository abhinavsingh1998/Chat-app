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
            val bookingId: String? =null,
            @SerializedName("documentCategory")
            val documentCategory: String,
            @SerializedName("documentType")
            val documentType: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("status")
            val status: String? = null,
            @SerializedName("itemInternalId")
            val itemInternalId: Any?,
            @SerializedName("name")
            val name: String,
            @SerializedName("path")
            val path: String?,
            @SerializedName("paymentId")
            val paymentId: Any?,
            @SerializedName("projectId")
            val projectId: String,
            @SerializedName("userId")
            val userId: Any?
        )

        data class PaymentHistory(
            @SerializedName("crmBookingId")
            val crmBookingId: String,
            @SerializedName("crmId")
            val crmId: String,
            @SerializedName("document")
            val document: PaymentReceipt,
            @SerializedName("id")
            val id: Int,
            @SerializedName("investment")
            val investment: Investment,
            @SerializedName("paidAmount")
            val paidAmount: Int,
            @SerializedName("paymentDate")
            val paymentDate: String,
            @SerializedName("projectName")
            val projectName: String,
            @SerializedName("userId")
            val userId: String
        ) {
            data class PaymentReceipt(
                @SerializedName("id")
                val id: Int,
                @SerializedName("userId")
                val userId: String,
                @SerializedName("projectId")
                val projectId: String,
                @SerializedName("paymentId")
                val paymentId: String,
                @SerializedName("bookingId")
                val bookingId: String,
                @SerializedName("documentCategory")
                val documentCategory: String,
                @SerializedName("documentType")
                val documentType: String,
                @SerializedName("path")
                val path: String,
                @SerializedName("name")
                val name: String,
                @SerializedName("itemInternalId")
                val itemInternalId: String,
                @SerializedName("createdat")
                val createdAt: String,
                @SerializedName("updateAt")
                val updateAt: String
            )

            data class Investment(
                @SerializedName("crmBookingId")
                val crmBookingId: String,
                @SerializedName("crmInventory")
                val crmInventory: CrmInventory
            ) {
                data class CrmInventory(
                    @SerializedName("createdAt")
                    val createdAt: String,
                    @SerializedName("crmId")
                    val crmId: String,
                    @SerializedName("crmInventoryBucketId")
                    val crmInventoryBucketId: String?,
                    @SerializedName("crmLaunchPhaseId")
                    val crmLaunchPhaseId: String,
                    @SerializedName("crmProjectId")
                    val crmProjectId: String,
                    @SerializedName("crmReraPhaseId")
                    val crmReraPhaseId: String?,
                    @SerializedName("id")
                    val id: Int,
                    @SerializedName("name")
                    val name: String,
                    @SerializedName("updatedAt")
                    val updatedAt: String
                )
            }
        }


    }
}



