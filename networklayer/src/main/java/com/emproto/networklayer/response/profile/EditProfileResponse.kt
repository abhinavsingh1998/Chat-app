package com.emproto.networklayer.response.profile

data class EditProfileResponse(
    val code: Int,
    val `data`: Data,
    val message: String
)