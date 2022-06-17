package com.emproto.networklayer.response.profile

data class ProfilePictureResponse(
    val code: Int,
    val `data`: DataView,
    val message: String
)
