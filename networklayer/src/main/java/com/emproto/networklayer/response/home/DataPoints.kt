package com.emproto.networklayer.response.home

data class DataPoints(
    val dataPointType: String,
    val points: List<Point>
)