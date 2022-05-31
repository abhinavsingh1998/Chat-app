package com.emproto.networklayer.response.marketingUpdates

import java.io.Serializable

data class DetailedInfo(
    val description: String,
    val media: Media
):Serializable