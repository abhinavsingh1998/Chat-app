package com.emproto.networklayer.response.bookingjourney

import com.emproto.networklayer.response.portfolio.ivdetails.InvestmentInformation
import com.emproto.networklayer.response.portfolio.ivdetails.ProjectExtraDetails

data class Data(
    val bookingJourney: BookingJourney,
    var investmentInformation: InvestmentInformation
)