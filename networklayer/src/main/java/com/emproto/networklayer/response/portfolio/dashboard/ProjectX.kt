package com.emproto.networklayer.response.portfolio.dashboard

data class ProjectX(
    val address: Address,
    val areaStartingFrom: String,
    val generalInfoEscalationGraph: GeneralInfoEscalationGraph,
    val id: Int,
    val launchName: String,
    val priceStartingFrom: String,
    val projectIcon: ProjectIcon,
//    val latitude: String,
//    val longitude: String,
//    val altitude: String
)