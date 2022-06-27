package com.emproto.networklayer.response.investment

data class GeneralInfoEscalationGraph(
    val title:String,
    val dataPoints: DataPoints,
    val estimatedAppreciation: Double,
    val xAxisDisplayName: String,
    val yAxisDisplayName: String
)
