package com.emproto.networklayer.response.investment

data class InvestmentPromisesResponse(
    val code: Int,
    val `data`: List<PmData>,
    val message: String,
    val pageIndex: Any,
    val pageSize: Any,
    val totalCount: Int,
    val totalPages: Any
)