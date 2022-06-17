package com.emproto.networklayer.response.watchlist

import java.io.Serializable

data class Data(
    val project: Project,
    val watchlist: Watchlist
):Serializable