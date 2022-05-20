package com.emproto.networklayer.response.investment

data class AllProjectsResponse(
    val code: Int,
    val `data`: List<ApData>,
    val message: String,
    val pageIndex: Any,
    val pageSize: Any,
    val totalCount: Int,
    val totalPages: Any
)