package com.emproto.networklayer.response.portfolio.prtimeline

import com.emproto.networklayer.response.home.Address
import com.emproto.networklayer.response.investment.ReraDetails

data class Data(
    val areaStartingFrom: String,
    val createdAt: String,
    val id: Int,
    val isEscalationGraphActive: Boolean,
    val isInventoryBucketActive: Boolean,
    val isKeyPillarsActive: Boolean,
    val isLatestMediaGalleryActive: Boolean,
    val isLocationInfrastructureActive: Boolean,
    val isOffersAndPromotionsActive: Boolean,
    val latestMediaGalleryHeading: String,
    val launchName: String,
    val numberOfSimilarInvestmentsToShow: Any,
    val priceStartingFrom: String,
    val projectId: Int,
    val projectTimelines: List<ProjectTimeline>,
    val shortDescription: String,
    val status: String,
    val updatedAt: String,
    val address: Address,
    val reraDetails: ReraDetails,

    )