package com.emproto.networklayer.response.portfolio.dashboard

data class Investment(
    val crmProjectId: String,
    val id: Int,
    val inventoryBucket: String,
    val isBookingComplete: Boolean,
    val ownershipDate: String,
    val userId: String,
    val actionItemCount: Int,
    val inventoryId: String,
    val areaSqFt: Double,
    val amountInvested: Double,
    val projectIea: String,
    val allocationDate: String

)