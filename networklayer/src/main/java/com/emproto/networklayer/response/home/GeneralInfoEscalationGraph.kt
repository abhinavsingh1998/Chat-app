package com.emproto.networklayer.response.home

data class GeneralInfoEscalationGraph(
    val dataPoints: DataPoints,
    val estimatedAppreciation: Int,
    val xAxisDisplayName: String,
    val yAxisDisplayName: String
)