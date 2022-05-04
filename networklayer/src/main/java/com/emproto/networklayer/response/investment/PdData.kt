package com.emproto.networklayer.response.investment

data class PdData(
    val address: Address,
    val areaRange: AreaRange,
    val createdAt: String,
    val fomoContent: FomoContent,
    val generalInfoEscalationGraph: GeneralInfoEscalationGraph,
    val id: Int,
    val isEscalationGraphActive: Boolean,
    val isInventoryBucketActive: Boolean,
    val isKeyPillarsActive: Boolean,
    val isLocationInfrastructureActive: Boolean,
    val isOffersAndPromotionsActive: Boolean,
    val keyPillars: KeyPillars,
    val launchName: String,
    val locationInfrastructure: LocationInfrastructure,
    val offersAndPromotions: OffersAndPromotions,
    val priceRange: PriceRange,
    val projectId: Int,
    val shortDescription: String,
    val status: String,
    val updatedAt: String,
    val mediaGalleries:List<MediaGallery>,
    val opprotunityDocs:List<OpprotunityDoc>
)