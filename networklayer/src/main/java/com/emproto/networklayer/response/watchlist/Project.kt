package com.emproto.networklayer.response.watchlist

import java.io.Serializable

data class Project(
    val address: Address,
    val areaStartingFrom: String,
    val fomoContent: FomoContent,
    val id: Int,
    val launchName: String,
    val priceStartingFrom: String,
    val projectIcon: ProjectIcon,
    val shortDescription: String,
    val estimatedAppreciation: Double
) : Serializable