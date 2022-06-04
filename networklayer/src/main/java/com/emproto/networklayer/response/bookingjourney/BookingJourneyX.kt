package com.emproto.networklayer.response.bookingjourney

data class BookingJourneyX(
    val afsStatus: Boolean,
    val amountPending: Int,
    val createdAt: String,
    val id: Int,
    val investmentId: Int,
    val isPOAAlloted: Boolean,
    val isPOARequired: Boolean,
    val paidAmount: Int,
    val updatedAt: String
)