package com.emproto.networklayer.response.home

import java.io.Serializable

data class GeneralInfoEscalationGraph(
    val dataPoints: DataPoints,
    val estimatedAppreciation: Int,
    val xAxisDisplayName: String,
    val yAxisDisplayName: String
):Serializable