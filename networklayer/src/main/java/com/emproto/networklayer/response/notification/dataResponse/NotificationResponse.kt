package com.emproto.networklayer.response.notification.dataResponse

data class NotificationResponse(
    val code: Int,
    val `data`: List<Data>,
    val message: String,
    val totalCount : Int,
    val totalPages : Int,
    val pageSize : Int,
    val pageIndex : Int
)