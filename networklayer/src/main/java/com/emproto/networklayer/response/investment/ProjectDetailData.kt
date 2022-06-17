package com.emproto.networklayer.response.investment

data class ProjectDetailData(
    val address: PdAddress,
    val areaRange: PdAreaRange,
    val createdAt: String,
    val fomoContent: PdFomoContent,
    val generalInfoEscalationGraph: PdGeneralInfoEscalationGraph,
    val id: Int,
    val inventoryBucketContents: List<PdInventoryBucketContent>,
    val isEscalationGraphActive: Boolean,
    val isInventoryBucketActive: Boolean,
    val isKeyPillarsActive: Boolean,
    val isLocationInfrastructureActive: Boolean,
    val isOffersAndPromotionsActive: Boolean,
    val keyPillars: PdKeyPillars,
    val launchName: String,
    val locationInfrastructure: PdLocationInfrastructure,
    val mediaGalleries: List<PdMediaGallery>,
    val offersAndPromotions: PdOffersAndPromotions,
    val opprotunityDocs: List<PdOpprotunityDoc>,
    val priceRange: PdPriceRange,
    val projectId: Int,
    val projectTimelines: List<PdProjectTimeline>,
    val shortDescription: String,
    val status: String,
    val updatedAt: String
)
