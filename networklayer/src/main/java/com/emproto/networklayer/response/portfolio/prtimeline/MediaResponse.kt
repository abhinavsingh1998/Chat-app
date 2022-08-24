package com.emproto.networklayer.response.portfolio.prtimeline

data class MediaResponse(
    val code: Int,
    val `data`: List<mData>,
    val message: String
)

data class mData(
    val category: String,
    val isPageWidthEnabled: Boolean,
    val mediaContent: MediaContent,
    val mediaContentType: String,
    val name: String,
    val status: String
)

data class MediaContent(
    val key: String,
    val name: String,
    val value: Value
)
