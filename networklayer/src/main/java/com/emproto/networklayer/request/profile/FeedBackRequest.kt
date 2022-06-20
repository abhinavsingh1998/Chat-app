package com.emproto.networklayer.request.profile

data class FeedBackRequest(
    val rating: Int,
    val categories: List<String>,
    val description: String,

)
