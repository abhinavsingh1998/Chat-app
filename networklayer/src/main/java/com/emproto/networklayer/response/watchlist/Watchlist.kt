package com.emproto.networklayer.response.watchlist

import java.io.Serializable

data class Watchlist(
    val id: Int,
    val projectContentId: String,
    val userId: Int
):Serializable