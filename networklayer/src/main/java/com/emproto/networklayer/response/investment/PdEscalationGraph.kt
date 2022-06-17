package com.emproto.networklayer.response.investment

data class PdEscalationGraph(
    val dataPoints: DataPointsX,
    val projectEstimatedAppreciation: Int,
    val xAxisDisplayName: String,
    val yAxisDisplayName: String
)