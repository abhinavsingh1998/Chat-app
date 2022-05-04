package com.emproto.networklayer.response.responsee

data class PageManagementsOrNewInvestment(
    val address: Address,
    val areaRange: AreaRange,
    val fomoContent: FomoContent,
    val generalInfoEscalationGraph: GeneralInfoEscalationGraph,
    val id: Int,
    val isEscalationGraphActive: Boolean,
    val launchName: String,
    val mediaGalleries: List<MediaGallery>,
    val pageManagementAndNewInvestments: PageManagementAndNewInvestments,
    val priceRange: PriceRange,
    val shortDescription: String
)