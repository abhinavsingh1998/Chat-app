package com.emproto.networklayer.response.profile

data class AllProjectsResponse(
    val code: Int,
    val data: List<DataXXX>,
    val message: String,
    val pageIndex: Any,
    val pageSize: Any,
    val totalCount: Int,
    val totalPages: Any
)