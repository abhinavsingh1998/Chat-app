package com.emproto.networklayer.response.marketingUpdates

data class LatestUpdatesResponse(
    val code: Int,
    val `data`: List<Data>,
    val message: String,
    val pageIndex: Int,
    val pageSize: Int,
    val totalCount: Int,
    val totalPages: Int
)