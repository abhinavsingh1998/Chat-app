package com.emproto.networklayer.response.portfolio.ivdetails

import com.emproto.networklayer.response.promises.HomePagesOrPromise

data class ProjectPromises(
    val code: Int,
    val `data`: List<HomePagesOrPromise>,
    val message: String,
    val pageIndex: Any,
    val pageSize: Any,
    val totalCount: Int,
    val totalPages: Any
)