package com.emproto.networklayer.response.investment

data class EscalationGraph(
    val title: String,
    val dataPoints: DataPoints,
    val projectEstimatedAppreciation: Int,
    val xAxisDisplayName: String,
    val yAxisDisplayName: String
)