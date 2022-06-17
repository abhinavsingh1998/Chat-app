package com.emproto.networklayer.response.login

data class VerifyOtpResponse(
    val code: Int,
    val message: String,
    val token: String,
    val user: User
)
