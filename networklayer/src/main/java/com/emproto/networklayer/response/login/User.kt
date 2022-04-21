package com.emproto.networklayer.response.login

data class User(
    val address: String,
    val contactType: String,
    val countryCode: String,
    val createdAt: String,
    val crmId: Int,
    val dateOfBirth: String,
    val email: String,
    val firstName: String,
    val gender: Int,
    val id: Int,
    val lastName: String,
    val otp: String,
    val otpVerified: Boolean,
    val phoneNumber: String,
    val profilePictureUrl: String,
    val status: String,
    val updatedAt: String,
    val whatsappConsent: Boolean
)