package com.emproto.networklayer.response.watchlist

import java.io.Serializable

data class Value(
    val height: Int,
    val mediaType: String,
    val size: Double,
    val url: String,
    val width: Int
):Serializable