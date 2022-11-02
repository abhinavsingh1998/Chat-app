package com.emproto.networklayer.response.sendMessage

data class Option(
    val action: Int,
    val actionType: Int,
    val optionNumber: Int,
    val text: String
)