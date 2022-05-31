package com.emproto.networklayer.response.investment

data class VideoCallResponse(
    val code: Int,
    val `data`: vData,
    val message: String
)

data class vData(
    val caseType: String,
    val countryCode: String,
    val createdAt: String,
    val description: String,
    val email: Any,
    val id: Int,
    val issueType: String,
    val phoneNumber: String,
    val projectId: Int,
    val status: Any,
    val updatedAt: String,
    val userId: Any
)