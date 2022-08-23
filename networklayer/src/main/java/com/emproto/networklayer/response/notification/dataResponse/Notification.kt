package com.emproto.networklayer.response.notification.dataResponse

data class Notification(
    val createdAt: String,
    val id: Int,
    val notificationDescription: NotificationDescription,
    val notificationTemplate: String,
    val targetPage: Int,
    val notificationType: String,
    val projectId: Int,
    val updatedAt: String
)