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
    val id: Int?=null,
    val inventoryBucketContents: List<Inventory>?=null,
    val otherSectionHeadings: OtherSectionHeadings?=null
)

data class Inventory(
    val areaRange: AreaRange?=null,
    val createdAt: String?=null,
    val crmInventoryBucketId: Int?=null,
    val id: Int?=null,
    var isApplied: Boolean,
    val name: String?=null,
    val priceRange: PriceRange?=null,
    val projectContentId: Int?=null,
    val shortDescription: String?=null,
    val updatedAt: String?=null,
    var isSoldOut:Boolean
)

data class OtherSectionHeadings(
    val droneVideos: DroneVideos?=null,
    val promises: Promises?=null,
    val testimonials: Testimonials?=null,
    val inventoryBucketContents: InventoryBucketContents?=null
)