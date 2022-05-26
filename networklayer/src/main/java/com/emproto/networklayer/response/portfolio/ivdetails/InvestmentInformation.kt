package com.emproto.networklayer.response.portfolio.ivdetails

data class InvestmentInformation(
    val agreementValue: Int,
    val amountInvested: Int,
    val areaSqFt: Int,
    val createdAt: String,
    val crmProjectId: String,
//    val documentLinks: List<String>,
    val id: Int,
    val inventoryBucket: String,
    val inventoryId: String,
    val isCompleted: Boolean,
    val otherExpenses: Int,
    val ownedSince: String,
    val owners: String,
    val registryAmount: Int,
    val updatedAt: String,
    val userId: String,
    val bookingJourney: BookingJourney
)