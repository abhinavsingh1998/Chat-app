package com.emproto.networklayer.response.investment

data class PageManagementsOrCollectionOneModel(
    val areaRange: AreaRange,
    val fomoContent: FomoContent,
    val generalInfoEscalationGraph: GeneralInfoEscalationGraph,
    val isEscalationGraphActive: Boolean,
    val launchName: String,
    val mediaGalleries: List<MediaGallery>,
    val priceRange: PriceRange,
    val shortDescription: String
)