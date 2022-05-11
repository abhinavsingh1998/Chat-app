package com.emproto.networklayer.response.portfolio.ivdetails

data class ProjectPromises(
    val code: Int,
    val `data`: List<DataX>,
    val message: String,
    val pageIndex: Any,
    val pageSize: Any,
    val totalCount: Int,
    val totalPages: Any
)