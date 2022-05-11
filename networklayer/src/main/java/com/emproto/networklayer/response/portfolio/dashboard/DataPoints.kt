package com.emproto.networklayer.response.portfolio.dashboard

data class DataPoints(
    val dataPointType: String,
    val points: List<Point>
)