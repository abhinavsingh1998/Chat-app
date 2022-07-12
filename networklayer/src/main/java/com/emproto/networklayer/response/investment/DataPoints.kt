package com.emproto.networklayer.response.investment

import java.io.Serializable

data class DataPoints(
    val dataPointType: String,
    val points: List<Point>
): Serializable