package com.emproto.networklayer.request.profile

data class FeedBackRequest(
    val categories: List<String>,
    val description: String,
    val rating: Int
)
