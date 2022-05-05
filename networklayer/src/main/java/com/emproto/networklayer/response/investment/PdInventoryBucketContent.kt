package com.emproto.networklayer.response.investment

data class PdInventoryBucketContent(
    val areaRange: AreaRangeX,
    val createdAt: String,
    val id: Int,
    val inventoryBucketId: Int,
    val name: String,
    val priceRange: PdPriceRange,
    val projectContentId: Int,
    val shortDescription: String,
    val updatedAt: String
)