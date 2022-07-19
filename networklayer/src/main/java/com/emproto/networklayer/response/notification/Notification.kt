package com.emproto.networklayer.response.notification

data class Notification(
    val createdAt: String,
    val id: Int,
    val notificationDescription: NotificationDescription,
    val notificationTemplate: String,
    val notificationType: String,
    val projectId: Int,
    val updatedAt: String
)