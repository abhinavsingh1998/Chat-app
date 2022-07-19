package com.emproto.networklayer.response.notification

data class NotificationResponse(
    val code: Int,
    val `data`: List<Data>,
    val message: String
)