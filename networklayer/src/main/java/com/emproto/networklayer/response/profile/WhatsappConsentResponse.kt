package com.emproto.networklayer.response.profile

data class WhatsappConsentResponse(
    val code: Int,
    val `data`: WcData,
    val message: String
)

data class WcData(
    val code: Int,
    val `data`: DataXW,
    val message: String
)

data class DataXW(
    val city: String,
    val contactType: String,
    val countryCode: String,
    val crmId: Any,
    val dateOfBirth: String,
    val email: String,
    val firstName: String,
    val gender: String,
    val houseNumber: String,
    val id: Int,
    val lastName: String,
    val locality: String,
    val otpVerified: Boolean,
    val phoneNumber: String,
    val pincode: String,
    val profilePictureUrl: Any,
    val state: String,
    val status: String,
    val streetAddress: String,
    val whatsappConsent: Boolean
)