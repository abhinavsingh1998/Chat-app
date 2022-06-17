package com.emproto.networklayer.response.login

data class AddNameResponse(
    val code: Int,
    val `data`: Data,
    val message: String
)