package com.emproto.networklayer.request.investment

data class AddInventoryBody(
    val inventoryBucketId: Int,
    val launchPhaseId: Int
)