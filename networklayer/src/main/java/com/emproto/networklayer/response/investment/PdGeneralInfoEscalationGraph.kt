package com.emproto.networklayer.response.investment

data class PdGeneralInfoEscalationGraph(
    val dataPoints: PdDataPoints,
    val estimatedAppreciation: Int,
    val xAxisDisplayName: String,
    val yAxisDisplayName: String
)