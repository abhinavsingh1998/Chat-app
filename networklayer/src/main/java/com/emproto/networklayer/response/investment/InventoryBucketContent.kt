package com.emproto.networklayer.response.investment

data class InventoryBucketContent(
    val areaRange: AreaRange,
    val createdAt: String,
    val id: Int,
    val inventoryBucketId: Int,
    val name: String,
    val priceRange: PriceRange,
    val projectContentId: Int,
    val shortDescription: String,
    val updatedAt: String
)

