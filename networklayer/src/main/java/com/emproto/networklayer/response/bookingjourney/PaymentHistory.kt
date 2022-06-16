package com.emproto.networklayer.response.bookingjourney

data class PaymentHistory(
    val createdAt: String,
    val crmBookingId: String,
    val crmId: String,
    val document: Document,
    val id: Int,
    val paidAmount: Double,
    val paymentDate: String,
    val projectName: String,
    val updatedAt: String,
    val userId: String
)