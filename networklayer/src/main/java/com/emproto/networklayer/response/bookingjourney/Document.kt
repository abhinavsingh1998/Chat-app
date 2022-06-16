package com.emproto.networklayer.response.bookingjourney

data class Document(
    val bookingId: String,
    val createdAt: String,
    val documentCategory: String,
    val documentType: String,
    val id: Int,
    val itemInternalId: Any,
    val name: String,
    val path: String,
    val paymentId: String,
    val projectId: String,
    val updatedAt: String,
    val userId: String
)