package com.emproto.networklayer.response.home

import java.io.Serializable

data class DataPoints(
    val dataPointType: String,
    val points: List<Point>
):Serializable