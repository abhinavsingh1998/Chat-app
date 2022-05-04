package com.emproto.networklayer.request.login

data class OtpVerifyRequest(
    val otpNumber: String,
    val phoneNumber: String,
    val whatsappConsent: Boolean,
    val countryCode: String
)