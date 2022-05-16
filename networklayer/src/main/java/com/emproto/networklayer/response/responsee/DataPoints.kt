package com.emproto.networklayer.response.responsee

data class DataPoints(
    val dataPointType: String,
    val points: List<Point>
)