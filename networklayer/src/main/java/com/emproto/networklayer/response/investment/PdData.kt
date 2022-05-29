package com.emproto.networklayer.response.investment

import com.emproto.networklayer.response.watchlist.Data
import com.emproto.networklayer.response.watchlist.Watchlist
import com.emproto.networklayer.response.watchlist.WatchlistData

data class PdData(
    val address: Address,
    val areaStartingFrom: String,
    val createdAt: String,
    val fomoContent: FomoContent,
    val generalInfoEscalationGraph: GeneralInfoEscalationGraph,
    val id: Int,
    val inventoryBucketContents: List<InventoryBucketContent>,
    val isEscalationGraphActive: Boolean,
    val isInventoryBucketActive: Boolean,
    val isKeyPillarsActive: Boolean,
    val isLatestMediaGalleryActive: Boolean,
    val isLocationInfrastructureActive: Boolean,
    val isOffersAndPromotionsActive: Boolean,
    val keyPillars: KeyPillars,
    val latestMediaGalleryHeading: String,
    val latestMediaGalleryOrProjectContent: List<LatestMediaGalleryOrProjectContent>,
    val launchName: String,
    val locationInfrastructure: LocationInfrastructure,
    val mediaGalleryOrProjectContent: List<MediaGalleryOrProjectContent>,
    val numberOfSimilarInvestmentsToShow: Any,
    val offersAndPromotions: OffersAndPromotions,
    val opprotunityDocs: List<OpprotunityDoc>,
    val priceStartingFrom: String,
    val projectContentsAndFaqs: List<ProjectContentsAndFaq>,
    val projectCoverImages: ProjectCoverImages,
    val projectId: Int,
    val projectTimelines: List<ProjectTimeline>,
    val reraDetails: ReraDetails,
    val shortDescription: String,
    val similarInvestments: List<SimilarInvestment>,
    val status: String,
    val updatedAt: String,
    var watchlist:List<Data>,
    var inventoriesList:IvData
)