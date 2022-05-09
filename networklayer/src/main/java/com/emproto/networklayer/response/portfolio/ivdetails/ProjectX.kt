package com.emproto.networklayer.response.portfolio.ivdetails

data class ProjectX(
    val address: Address,
    val areaStartingFrom: String,
    val generalInfoEscalationGraph: GeneralInfoEscalationGraph,
    val id: Int,
    val launchName: String,
    val priceStartingFrom: String,
    val projectCoverImages: ProjectCoverImages
)