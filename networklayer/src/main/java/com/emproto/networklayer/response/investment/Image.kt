package com.emproto.networklayer.response.investment

import java.io.Serializable

data class Image(
    val mediaContent: MediaContent,
    val mediaContentType: String,
    val name: String,
    val status: String
): Serializable