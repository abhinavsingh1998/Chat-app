package com.emproto.networklayer.response.home

data class PortFolioData(
    val amountInvested: Double,
    val amountPending: Double,
    val investmentCount: Int,
    val totalAreaSqFt: Double
)
