package com.emproto.networklayer.response.home

data class PageManagementsOrNewInvestment(
    val areaRange: AreaRange,
    val fomoContent: FomoContent,
    val generalInfoEscalationGraph: GeneralInfoEscalationGraph,
    val isEscalationGraphActive: Boolean,
    val launchName: String,
    val pageManagementAndNewInvestments: PageManagementAndNewInvestments,
    val priceRange: PriceRange,
    val shortDescription: String
)