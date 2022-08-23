package com.emproto.networklayer.response.portfolio.prtimeline

data class Values(
    val medias: Medias,
    val percentage: Double,
    val status: String,
    val toolTipDetails: String,
    val webLink: String,
    val reraLink: String,
    val dateOfCompletion: String,
    val displayName: String,
    val isCtaActive: Boolean
)