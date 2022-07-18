package com.emproto.networklayer.response.login


import com.google.gson.annotations.SerializedName

data class User (
        @SerializedName("amountInvestedCompletedInvestments")
        val amountInvestedCompletedInvestments: Any?,
        @SerializedName("amountInvestedOngoingInvestments")
        val amountInvestedOngoingInvestments: Any?,
        @SerializedName("amountPendingOngoingInvestments")
        val amountPendingOngoingInvestments: Any?,
        @SerializedName("areaSqftCompletedInvestments")
        val areaSqftCompletedInvestments: Any?,
        @SerializedName("areaSqftOngoingInvestments")
        val areaSqftOngoingInvestments: Any?,
        @SerializedName("city")
        val city: String,
        @SerializedName("contactType")
        val contactType: Int,
        @SerializedName("country")
        val country: String,
        @SerializedName("countryCode")
        val countryCode: String,
        @SerializedName("crmId")
        val crmId: String,
        @SerializedName("dateOfBirth")
        val dateOfBirth: String,
        @SerializedName("deviceTokens")
        val deviceTokens: List<String>,
        @SerializedName("email")
        val email: String,
        @SerializedName("firstName")
        val firstName: String,
        @SerializedName("gender")
        val gender: String,
        @SerializedName("houseNumber")
        val houseNumber: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("isFacilityVisible")
        val isFacilityVisible: Boolean,
        @SerializedName("isInvalidOtpCount")
        val isInvalidOtpCount: Int,
        @SerializedName("isKycComplete")
        val isKycComplete: Boolean,
        @SerializedName("isProfileComplete")
        val isProfileComplete: Boolean,
        @SerializedName("lastBlockedAt")
        val lastBlockedAt: Any?,
        @SerializedName("lastName")
        val lastName: String,
        @SerializedName("locality")
        val locality: String,
        @SerializedName("numberOfProductsCompletedInvestments")
        val numberOfProductsCompletedInvestments: Any?,
        @SerializedName("numberOfProductsOngoingInvestments")
        val numberOfProductsOngoingInvestments: Any?,
        @SerializedName("phoneNumber")
        val phoneNumber: String,
        @SerializedName("pincode")
        val pincode: String,
        @SerializedName("profilePictureUrl")
        val profilePictureUrl: Any?,
        @SerializedName("sendOtpCount")
        val sendOtpCount: Int,
        @SerializedName("showPushNotifications")
        val showPushNotifications: Boolean,
        @SerializedName("state")
        val state: String,
        @SerializedName("streetAddress")
        val streetAddress: String,
        @SerializedName("verificationStatus")
        val verificationStatus: String,
        @SerializedName("whatsappConsent")
        val whatsappConsent: Boolean
    )
