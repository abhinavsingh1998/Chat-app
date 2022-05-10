package com.emproto.networklayer.response.portfolio.ivdetails

data class InvestmentInformation(
    val agreementValue: Int,
    val allocationDate: String,
    val amountInvested: Int,
    val amountPending: Int,
    val areaSqFt: Int,
    val bookingStatus: String,
    val createdAt: String,
    val crmProjectId: Int,
    val documentLinks: List<String>,
    val id: Int,
    val inventoryBucket: String,
    val inventoryId: String,
    val isCompleted: Boolean,
    val otherExpenses: Int,
    val ownedSince: String,
    val owners: String,
    val ownershipDate: String,
    val paidAmount: Int,
    val possesionDate: String,
    val registryAmount: Int,
    val updatedAt: String,
    val userId: Int
)