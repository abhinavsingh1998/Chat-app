package com.emproto.networklayer.response.profile

data class ProfileResponse(
    val code: Int,
    val `data`: Data,
    val message: String
)