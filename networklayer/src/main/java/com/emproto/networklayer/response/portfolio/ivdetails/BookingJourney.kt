package com.emproto.networklayer.response.portfolio.ivdetails

data class BookingJourney(
    val allocationDate: String,
    val amountPending: Double,
    val bookingId: String,
    val bookingStatus: String,
    val createdAt: String,
    val documentLinks: List<Any>,
    val id: Int,
    val ownershipDate: String,
    val paidAmount: Double,
    val possesionDate: String,
    val updatedAt: String
)