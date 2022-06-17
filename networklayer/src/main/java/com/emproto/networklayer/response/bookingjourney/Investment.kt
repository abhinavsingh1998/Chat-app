package com.emproto.networklayer.response.bookingjourney

import com.emproto.networklayer.response.portfolio.dashboard.Address
import com.emproto.networklayer.response.portfolio.ivdetails.ProjectExtraDetails

data class Investment(
    val agreementValue: Double,
    val allocationDate: String,
    val areaSqFt: Double,
    val bookingId: String,
    val bookingJourney: BookingJourneyX,
    val bookingStatus: String,
    val camCharges: Double,
    val camGST: Any,
    val corpusCharges: Double,
    val corpusGST: Any,
    val createdAt: String,
    val crmBookingId: String,
    val crmInventoryId: Any,
    val crmProjectId: String,
    val id: Int,
    val infraGST: Double,
    val infraValue: Double,
    val inventoryBucket: String,
    val inventoryId: String,
    val isBookingComplete: Boolean,
    val owners: String,
    val ownershipDate: Any,
    val paymentSchedules: List<PaymentSchedule>,
    val possesionDate: Any,
    val registrationCharges: Any,
    val registrationDate: Any,
    val registrationNumber: Any,
    val registryAmount: Double,
    val sdrCharges: Double,
    val updatedAt: String,
    val userId: String,
    //for booking journey
    var extraDetails: ProjectExtraDetails
)