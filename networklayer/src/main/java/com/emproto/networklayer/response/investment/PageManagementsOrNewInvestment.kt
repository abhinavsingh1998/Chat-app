package com.emproto.networklayer.response.investment

data class PageManagementsOrNewInvestment(
    val id:Int,
    val areaRange: AreaRange,
    val fomoContent: FomoContent,
    val address:Address,
    val generalInfoEscalationGraph: GeneralInfoEscalationGraph,
    val isEscalationGraphActive: Boolean,
    val launchName: String,
    val pageManagementAndNewInvestments: PageManagementAndNewInvestments,
    val priceRange: PriceRange,
    val shortDescription: String,
    val mediaGalleries: List<MediaGallery>
)