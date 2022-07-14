package com.emproto.networklayer.response.portfolio.dashboard

import com.emproto.networklayer.response.investment.OtherSectionHeading

data class ProjectX(
    val address: Address,
    val areaStartingFrom: String,
    val generalInfoEscalationGraph: GeneralInfoEscalationGraph,
    val id: Int,
    val launchName: String,
    val priceStartingFrom: String,
    val projectIcon: ProjectIcon,
    val isSimilarInvestmentActive: Boolean,
    val numberOfSimilarInvestmentsToShow: Int,
    val similarInvestmentSectionHeading: String,
    val isEscalationGraphActive: Boolean,
    val isLatestMediaGalleryActive: Boolean,
    val latestMediaGalleryHeading: String,
    val otherSectionHeadings: OtherSectionHeading
//    val latitude: String,
//    val longitude: String,
//    val altitude: String
)

data class InvestmentHeadingDetails(
    val isSimilarInvestmentActive: Boolean,
    val numberOfSimilarInvestmentsToShow: Int,
    val similarInvestmentSectionHeading: String?,
    val isEscalationGraphActive: Boolean,
    val isLatestMediaGalleryActive: Boolean,
    val latestMediaGalleryHeading: String,
    val otherSectionHeadings: OtherSectionHeading
)