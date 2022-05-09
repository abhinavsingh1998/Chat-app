package com.emproto.networklayer.response.portfolio.ivdetails

data class GeneralInfoEscalationGraph(
    val dataPoints: DataPoints,
    val estimatedAppreciation: Int,
    val xAxisDisplayName: String,
    val yAxisDisplayName: String
)