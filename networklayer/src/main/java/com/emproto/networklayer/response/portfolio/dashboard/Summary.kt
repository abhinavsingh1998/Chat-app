package com.emproto.networklayer.response.portfolio.dashboard

data class Summary(
    val iea:String,
    val completed: Completed,
    val ongoing: Ongoing
)