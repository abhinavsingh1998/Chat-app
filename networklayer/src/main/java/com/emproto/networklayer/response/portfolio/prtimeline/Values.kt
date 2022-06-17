package com.emproto.networklayer.response.portfolio.prtimeline

data class Values(
    val dateOfCompletion: String,
    val displayName: String,
    val medias: Medias,
    val percentage: Double,
    val status: String,
    val toolTipDetails: String,
    val webLink: String
)