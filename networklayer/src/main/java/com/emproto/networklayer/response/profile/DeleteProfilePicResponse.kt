package com.emproto.networklayer.response.profile


import com.google.gson.annotations.SerializedName

data class DeleteProfilePicResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("message")
    val message: String
) {
    data class Data(
        @SerializedName("contactType")
        val contactType: String,
        @SerializedName("countryCode")
        val countryCode: Any?,
        @SerializedName("crmId")
        val crmId: Any?,
        @SerializedName("email")
        val email: Any?,
        @SerializedName("firstName")
        val firstName: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("lastName")
        val lastName: String,
        @SerializedName("otpVerified")
        val otpVerified: Boolean,
        @SerializedName("phoneNumber")
        val phoneNumber: String,
        @SerializedName("profilePictureUrl")
        val profilePictureUrl: Any?,
        @SerializedName("status")
        val status: Any?,
        @SerializedName("whatsappConsent")
        val whatsappConsent: Boolean
    )
}