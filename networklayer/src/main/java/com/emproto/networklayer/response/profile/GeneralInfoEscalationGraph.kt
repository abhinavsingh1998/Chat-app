package com.emproto.networklayer.response.profile

data class GeneralInfoEscalationGraph(
    val dataPoints: DataPoints,
    val estimatedAppreciation: Double,
    val title: String,
    val xAxisDisplayName: String,
    val yAxisDisplayName: String
)