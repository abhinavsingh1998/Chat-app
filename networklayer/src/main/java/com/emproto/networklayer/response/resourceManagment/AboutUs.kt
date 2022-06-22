package com.emproto.networklayer.response.resourceManagment

data class AboutUs(
    val aboutHoabl: AboutHoabl,
    val corporatePhilosophy: CorporatePhilosophy,
    val foundersVision: FoundersVision,
    val isAboutHoablActive: Boolean,
    val isCorporatePhilosophyActive: Boolean,
    val isFoundersVisionActive: Boolean,
    val isProductCategoryActive: Boolean,
    val isStatsOverviewActive: Boolean,
    val productCategory: ProductCategory,
    val statsOverview: StatsOverview
)