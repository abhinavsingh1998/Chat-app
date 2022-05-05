package com.emproto.networklayer.response.investment

data class PdCoverImage(
    val mediaContent: PdMediaContent,
    val mediaContentType: String,
    val name: String,
    val status: String
)