package com.emproto.networklayer.response.investment

data class PageManagementsOrCollectionOneModel(
    val id:Int,
    val areaRange: AreaRange,
    val fomoContent: FomoContent,
    val address:Address,
    val generalInfoEscalationGraph: GeneralInfoEscalationGraph,
    val isEscalationGraphActive: Boolean,
    val launchName: String,
    val mediaGalleries: List<MediaGallery>,
    val priceRange: PriceRange,
    val shortDescription: String
)