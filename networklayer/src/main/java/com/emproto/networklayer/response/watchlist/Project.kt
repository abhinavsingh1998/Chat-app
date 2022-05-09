package com.emproto.networklayer.response.watchlist

data class Project(
    val address: Address,
    val areaStartingFrom: String,
    val fomoContent: FomoContent,
    val id: Int,
    val launchName: String,
    val priceStartingFrom: String,
    val projectIcon: ProjectIcon,
    val shortDescription: String
)