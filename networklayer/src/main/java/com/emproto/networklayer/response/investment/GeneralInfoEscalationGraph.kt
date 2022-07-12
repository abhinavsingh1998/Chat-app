package com.emproto.networklayer.response.investment

import java.io.Serializable

data class GeneralInfoEscalationGraph(
    val title:String,
    val dataPoints: DataPoints,
    val estimatedAppreciation: Double,
    val xAxisDisplayName: String,
    val yAxisDisplayName: String
): Serializable
