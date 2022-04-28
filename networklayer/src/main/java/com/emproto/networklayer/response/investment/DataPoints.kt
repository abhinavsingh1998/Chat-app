package com.emproto.networklayer.response.investment

data class DataPoints(
    val dataPointType: String,
    val points: List<Point>
)