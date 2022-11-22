package com.emproto.networklayer.response.portfolio.ivdetails

import com.emproto.networklayer.response.documents.Data

data class Data(
    val investmentInformation: InvestmentInformation,
    val projectInformation: ProjectInformation,
    val projectPromises: ProjectPromises,
    var projectExtraDetails: ProjectExtraDetails
)