package com.emproto.networklayer.response.portfolio

data class DataPoints(
    val dataPointType: String,
    val points: List<Point>
)