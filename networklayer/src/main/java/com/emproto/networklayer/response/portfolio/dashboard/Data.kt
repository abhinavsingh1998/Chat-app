package com.emproto.networklayer.response.portfolio.dashboard

import com.emproto.networklayer.response.watchlist.Data

data class Data(
    val projects: List<Project>,
    val summary: Summary,
    var watchlist:List<Data>
)