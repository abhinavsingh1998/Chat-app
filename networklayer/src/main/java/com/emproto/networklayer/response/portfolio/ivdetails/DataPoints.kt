package com.emproto.networklayer.response.portfolio.ivdetails

data class DataPoints(
    val dataPointType: String,
    val points: List<Point>
)