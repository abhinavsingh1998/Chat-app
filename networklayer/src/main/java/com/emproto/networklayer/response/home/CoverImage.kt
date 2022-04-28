package com.emproto.networklayer.response.home

data class CoverImage(
    val mediaContent: MediaContent,
    val mediaContentType: String,
    val name: String,
    val status: String
)