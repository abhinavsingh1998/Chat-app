package com.emproto.networklayer.response.investment

data class PageManagementsOrCollectionTwoModel(
    val id:Int,
    val areaRange: PdAreaRange,
    val fomoContent: PdFomoContent,
    val address:PdAddress,
    val generalInfoEscalationGraph: PdGeneralInfoEscalationGraph,
    val isEscalationGraphActive: Boolean,
    val launchName: String,
    val mediaGalleries: List<PdMediaGallery>,
    val priceRange: PdPriceRange,
    val shortDescription: String
)