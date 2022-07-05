package com.emproto.networklayer.response.investment

data class GetInventoriesResponse(
    val code: Int,
    val `data`: IvData,
    val message: String
)

data class IvData(
    val projectContent: ProjectContent
)

data class ProjectContent(
    val id: Int,
    val inventoryBucketContents: List<Inventory>,
    val otherSectionHeadings: OtherSectionHeadings
)

data class Inventory(
    val areaRange: AreaRange,
    val createdAt: String,
    val crmInventoryBucketId: Int,
    val id: Int,
    val isApplied: Boolean,
    val name: String,
    val priceRange: PriceRange,
    val projectContentId: Int,
    val shortDescription: String,
    val updatedAt: String
)

data class OtherSectionHeadings(
    val droneVideos: DroneVideos,
    val promises: Promises,
    val testimonials: Testimonials,
    val inventoryBucketContents: InventoryBucketContents
)