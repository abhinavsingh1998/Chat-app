package com.emproto.networklayer.response.notification

data class NotificationDescription(
    val body: String,
    val media: Media,
    val title: String
)