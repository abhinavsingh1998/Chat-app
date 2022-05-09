package com.emproto.networklayer.response.portfolio.ivdetails

data class Ongoing(
    val amountPaid: Int,
    val amountPending: Int,
    val areaSqFt: Int,
    val count: Int
)