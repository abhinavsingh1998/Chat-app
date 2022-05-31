package com.emproto.networklayer.response.investment

data class GetInventoriesResponse(
    val code: Int,
    val `data`: IvData,
    val message: String
)

data class IvData(
    val `data`: List<Inventory>
)

data class Inventory(
    val appliedInventoryId: Any,
    val areaRange: AreaRange,
    val id: Int,
    val inventoryBucketDescription: String,
    val inventoryBucketName: String,
    val isApplied: Boolean,
    val priceRange: PriceRange,
    val userId: Any
)