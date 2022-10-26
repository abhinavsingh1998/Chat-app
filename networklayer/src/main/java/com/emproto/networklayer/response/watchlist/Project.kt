package com.emproto.networklayer.response.watchlist

import java.io.Serializable

data class Project(
    val address: Address,
    val areaStartingFrom: String,
    val fomoContent: FomoContent,
    val id: Int,
    val isSoldOut:Boolean,
    val launchName: String,
    val priceStartingFrom: String,
    val projectIcon: ProjectIcon,
    val shortDescription: String,
    val estimatedAppreciation: Double,
    val projectCoverImages: ProjectCoverImages
) : Serializable

data class ProjectCoverImages(
    val homePageMedia: ProjectIcon,
    val portfolioPageMedia: ProjectIcon,
    val collectionListViewPageMedia: ProjectIcon
)