package com.emproto.networklayer.request.login

data class TroubleSigningRequest(
    val caseType: String,
    val countryCode: String,
    val description: String,
    val email: String,
    val issueType: String,
    val phoneNumber: String
)