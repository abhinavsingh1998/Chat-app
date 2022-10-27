package com.emproto.networklayer.response.portfolio.ivdetails

import com.emproto.networklayer.response.watchlist.ProjectCoverImages

data class SimilarInvestment(
    val address: Address,
    val areaStartingFrom: String,
    val estimatedAppreciation: Double,
    val fomoContent: FomoContent,
    val id: Int,
    val isSoldOut:Boolean,
    val launchName: String,
    val priceStartingFrom: String,
    val projectIcon: ProjectIcon,
    val shortDescription: String,
    val projectCoverImages: ProjectCoverImages
)