package com.emproto.networklayer.response.investment


data class PdDataPoints(
    val dataPointType: String,
    val points: List<PdPoint>
)