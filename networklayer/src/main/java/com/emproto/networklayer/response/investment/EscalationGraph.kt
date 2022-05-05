package com.emproto.networklayer.response.investment

data class EscalationGraph(
    val dataPoints: DataPoints,
    val projectEstimatedAppreciation: Int,
    val xAxisDisplayName: String,
    val yAxisDisplayName: String
)