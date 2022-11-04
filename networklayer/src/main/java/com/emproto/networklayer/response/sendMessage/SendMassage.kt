package com.emproto.networklayer.response.sendMessage

data class SendMassage(
    val code: Int,
    val `data`: Data,
    val message: String
)