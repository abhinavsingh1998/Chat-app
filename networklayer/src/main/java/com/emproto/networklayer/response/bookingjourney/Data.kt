package com.emproto.networklayer.response.bookingjourney

import com.emproto.networklayer.response.portfolio.ivdetails.InvestmentInformation

data class Data(
    val bookingJourney: BookingJourney,
    var investmentInformation: InvestmentInformation
)

data class BJHeader(
    val launchName: String,
    val address: String,
    val bookingStatus: Int,
    val ownerName: String,
    val inventoryData: String
)