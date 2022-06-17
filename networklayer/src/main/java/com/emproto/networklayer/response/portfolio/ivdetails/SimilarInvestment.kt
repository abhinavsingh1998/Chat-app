package com.emproto.networklayer.response.portfolio.ivdetails

data class SimilarInvestment(
    val address: Address,
    val areaStartingFrom: String,
    val estimatedAppreciation: Double,
    val fomoContent: FomoContent,
    val id: Int,
    val launchName: String,
    val priceStartingFrom: String,
    val projectIcon: ProjectIcon,
    val shortDescription: String
)