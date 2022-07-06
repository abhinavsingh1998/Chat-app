package com.emproto.networklayer.response.profile

import java.io.Serializable

data class ProfileResponse(
    val code: Int,
    val `data`: Data,
    val message: String
):Serializable