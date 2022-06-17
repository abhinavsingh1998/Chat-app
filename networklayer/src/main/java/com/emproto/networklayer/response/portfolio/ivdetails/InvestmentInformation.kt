package com.emproto.networklayer.response.portfolio.ivdetails

import com.emproto.networklayer.response.portfolio.dashboard.Address

data class InvestmentInformation(
    val agreementValue: Double,
    val amountInvested: Double,
    val areaSqFt: Double,
    val createdAt: String,
    val crmProjectId: String,
//    val documentLinks: List<String>,
    val id: Int,
    val inventoryBucket: String,
    val inventoryId: String,
    val isBookingComplete: Boolean,
    val otherExpenses: Double,
    val ownedSince: String,
    val owners: String,
    val registryAmount: Double,
    val updatedAt: String,
    val userId: String,
    val bookingJourney: BookingJourney,
    val paymentSchedules: List<PaymentSchedulesItem>,
    val possesionDate: String,
    val allocationDate: String,
    val ownershipDate: String,
    val bookingStatus: String,
    var launchName:String,
    var address: Address


    )