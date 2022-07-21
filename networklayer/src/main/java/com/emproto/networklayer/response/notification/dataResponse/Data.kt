package com.emproto.networklayer.response.notification.dataResponse

data class Data(
    val id: Int,
    val notification: Notification,
    val notificationId: Int,
    val readStatus: Boolean,
    val userId: Int
)