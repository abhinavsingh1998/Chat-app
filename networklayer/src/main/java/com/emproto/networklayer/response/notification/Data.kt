package com.emproto.networklayer.response.notification

data class Data(
    val id: Int,
    val notification: Notification,
    val notificationId: Int,
    val readStatus: Boolean,
    val userId: Int
)