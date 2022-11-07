package com.emproto.networklayer.response.bookingjourney

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PaymentReceipt(
    @SerializedName("applicationId")
    val applicationId: Any?,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("crmBookingId")
    val crmBookingId: String,
    @SerializedName("crmCustomerPaymentId")
    val crmCustomerPaymentId: String,
    @SerializedName("crmLaunchPhaseId")
    val crmLaunchPhaseId: String,
    @SerializedName("documentCategory")
    val documentCategory: Int,
    @SerializedName("documentType")
    val documentType: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("itemInternalId")
    val itemInternalId: Any?,
    @SerializedName("name")
    val name: String,
    @SerializedName("path")
    val path: String?,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("userId")
    val userId: String
):Serializable