package com.emproto.networklayer.response.portfolio.ivdetails

data class Data(
    val investmentInformation: InvestmentInformation,
    val projectInformation: ProjectInformation,
    val projectPromises: ProjectPromises,
    var projectExtraDetails: ProjectExtraDetails
)