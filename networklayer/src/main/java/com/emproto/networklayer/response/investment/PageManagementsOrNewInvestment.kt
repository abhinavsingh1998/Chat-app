package com.emproto.networklayer.response.investment

data class PageManagementsOrNewInvestments(
    val address: Address,
    val areaStartingFrom: String,
    val fomoContent: FomoContent,
    val generalInfoEscalationGraph: GeneralInfoEscalationGraph,
    val id: Int,
    val isEscalationGraphActive: Boolean,
    val launchName: String,
    val pageManagementAndNewInvestments: String,
    val priceStartingFrom: String,
    val projectCoverImages: ProjectCoverImages,
    val shortDescription: String
)