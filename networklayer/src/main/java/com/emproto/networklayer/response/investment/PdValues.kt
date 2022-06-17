package com.emproto.networklayer.response.investment

data class PdValues(
    val dateOfCompletion: String,
    val displayName: String,
    val medias: Medias,
    val percentage: Int,
    val status: String,
    val toolTipDetails: String,
    val webLink: String
)