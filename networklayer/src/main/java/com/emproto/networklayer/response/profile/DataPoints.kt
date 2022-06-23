package com.emproto.networklayer.response.profile

data class DataPoints(
    val dataPointType: String,
    val points: List<Point>
)