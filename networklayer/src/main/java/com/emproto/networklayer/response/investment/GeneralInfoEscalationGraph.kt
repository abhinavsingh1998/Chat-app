package com.emproto.networklayer.response.investment

data class GeneralInfoEscalationGraph(
    val dataPoints: DataPoints,
    val estimatedAppreciation: Double,
    val xAxisDisplayName: String,
    val yAxisDisplayName: String
)
