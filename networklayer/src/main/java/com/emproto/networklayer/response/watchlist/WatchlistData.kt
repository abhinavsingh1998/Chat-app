package com.emproto.networklayer.response.watchlist

data class WatchlistData(
    val code: Int,
    val `data`: List<Data>,
    val message: String
)