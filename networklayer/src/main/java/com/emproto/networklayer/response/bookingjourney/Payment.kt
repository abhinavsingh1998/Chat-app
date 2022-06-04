package com.emproto.networklayer.response.bookingjourney

data class Payment(
    val bookingAmount: Any,
    val bookingDate: Any,
    val createdAt: String,
    val crmBookingId: String,
    val crmLaunchPhaseId: String,
    val expectedAmount: Int,
    val id: Int,
    val isPaymentDone: Boolean,
    val paidAmount: Int,
    val paymentMilestone: String,
    val pendingAmount: Int,
    val targetDate: Any,
    val updatedAt: String,
    val userId: String
)