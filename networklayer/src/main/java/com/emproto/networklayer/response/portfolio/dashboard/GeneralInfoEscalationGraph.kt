package com.emproto.networklayer.response.portfolio.dashboard

data class GeneralInfoEscalationGraph(
    val dataPoints: DataPoints,
    val estimatedAppreciation: Double,
    val xAxisDisplayName: String,
    val yAxisDisplayName: String
)