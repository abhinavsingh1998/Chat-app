package com.emproto.networklayer.response.investment

import java.io.Serializable

data class AllProjectsResponse(
    val code: Int,
    val `data`: List<ApData>,
    val message: String,
    val pageIndex: Any,
    val pageSize: Any,
    val totalCount: Int,
    val totalPages: Any
):Serializable