package com.emproto.networklayer.response.login

data class TroubleSigningResponse(
    val code: Int,
    val `data`: DataX,
    val message: String
)