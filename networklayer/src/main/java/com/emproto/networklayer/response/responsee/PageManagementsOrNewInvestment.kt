package com.emproto.networklayer.response.responsee

data class PageManagementsOrNewInvestment(
    val areaRange: AreaRange,
    val fomoContent: FomoContent,
    val generalInfoEscalationGraph: GeneralInfoEscalationGraph,
    val isEscalationGraphActive: Boolean,
    val launchName: String,
    val mediaGalleries: List<MediaGallery>,
    val pageManagementAndNewInvestments: PageManagementAndNewInvestments,
    val priceRange: PriceRange,
    val shortDescription: String
)