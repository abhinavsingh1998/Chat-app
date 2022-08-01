package com.emproto.networklayer.response.home

data class PortFolioData(
    val amountInvested: Double,
    val amountPending: Int,
    val investmentCount: Int,
    val totalAreaSqFt: Int
)
