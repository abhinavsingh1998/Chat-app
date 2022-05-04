package com.emproto.networklayer.response.portfolio

data class Project(
    val areaRange: AreaRange,
    val crmProjectId: Int,
    val inventoryId: String,
    val isCompleted: Boolean,
    val ownershipDate: Any,
    val priceRange: PriceRange,
    val projectAddress: ProjectAddress,
    val projectGraph: ProjectGraph,
    val projectIcon: ProjectIcon,
    val projectName: String,
    val skuType: String
)