package com.emproto.networklayer.response.login

data class Data(
    val city: Any,
    val contactType: String,
    val country: Any,
    val countryCode: String,
    val crmId: Any,
    val dateOfBirth: Any,
    val email: Any,
    val firstName: String,
    val gender: Any,
    val houseNumber: Any,
    val id: Int,
    val isInvalidOtpCount: Int,
    val lastName: String,
    val locality: Any,
    val phoneNumber: String,
    val pinCode: Any,
    val profilePictureUrl: Any,
    val resendOtpCount: Int,
    val state: Any,
    val streetAddress: Any,
    val verificationStatus: String,
    val whatsappConsent: Boolean
)