package com.emproto.networklayer.response.profile
import com.google.gson.annotations.SerializedName


data class UploadKycResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String
) {
    data class Data(
        @SerializedName("response")
        val response: Response
    ) {
        data class Response(
            @SerializedName("data")
            val `data`: Data,
            @SerializedName("isUpdated")
            val isUpdated: Boolean
        ) {
            data class Data(
                @SerializedName("createdAt")
                val createdAt: String,
                @SerializedName("crmBookingId")
                val crmBookingId: Any?,
                @SerializedName("crmCustomerPaymentId")
                val crmCustomerPaymentId: Any?,
                @SerializedName("crmLaunchPhaseId")
                val crmLaunchPhaseId: Any?,
                @SerializedName("documentCategory")
                val documentCategory: Int,
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