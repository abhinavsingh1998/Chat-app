package com.emproto.networklayer.response.portfolio.dashboard

data class Investment(
    val crmProjectId: String,
    val id: Int,
    val inventoryBucket: String,
    val isCompleted: Boolean,
    val ownershipDate: String,
    val userId: String
)