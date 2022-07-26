package com.emproto.networklayer.response.portfolio.prtimeline

data class Section(
    val key: String,
    val values: Values,
    val percentage: Double,
    val status: String,
    val toolTipDetails: String,
    val webLink: String,
    val reraLink: String,
    val dateOfCompletion: String,
    val displayName: String,
)