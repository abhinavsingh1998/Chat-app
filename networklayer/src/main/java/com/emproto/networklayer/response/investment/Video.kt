package com.emproto.networklayer.response.investment

data class Video(
    val mediaContent: MediaContent,
    val mediaContentType: String,
    val name: String,
    val status: String
)