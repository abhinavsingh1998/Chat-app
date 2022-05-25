package com.emproto.networklayer.response.watchlist

import java.io.Serializable

data class Watchlist(
    val id: Int,
    val projectId: String,
    val userId: Int
):Serializable