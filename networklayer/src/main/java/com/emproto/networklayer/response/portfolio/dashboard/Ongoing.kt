package com.emproto.networklayer.response.portfolio.dashboard

data class Ongoing(
    val amountPaid: Double,
    val amountPending: Double,
    val areaSqFt: Int,
    val count: Int
)