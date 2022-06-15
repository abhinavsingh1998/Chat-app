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
):Serializable {
    data class Data(
        @SerializedName("documents")
        val documents: List<Document>,
        @SerializedName("paymentHistory")
        val paymentHistory: List<PaymentHistory>
    ):Serializable {
        data class Document(
            @SerializedName("bookingId")
            val bookingId: Any?,
            @SerializedName("documentCategory")
            val documentCategory: String?,
            @SerializedName("documentType")
            val documentType: String,
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
            val projectId: String,
            @SerializedName("userId")
            val userId: Any?
        ):Serializable

        data class PaymentHistory(
            @SerializedName("crmBookingId")
            val crmBookingId: String,
            @SerializedName("crmId")
            val crmId: Any?,
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
        ):Serializable {
            data class Investment(
                @SerializedName("crmBookingId")
                val crmBookingId: String,
                @SerializedName("crmInventory")
                val crmInventory: CrmInventory
            ):Serializable {
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
                ):Serializable
            }
        }
    }
}