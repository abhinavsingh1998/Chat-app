package com.emproto.networklayer.response.login

data class Data(
    val address: String,
    val contactType: String,
    val countryCode: String,
    val crmId: Int,
    val dateOfBirth: String,
    val email: String,
    val firstName: String,
    val gender: Int,
    val id: Int,
    val lastName: String,
    val otpVerified: Boolean,
    val phoneNumber: String,
    val profilePictureUrl: String,
    val status: String,
    val whatsappConsent: Boolean
)