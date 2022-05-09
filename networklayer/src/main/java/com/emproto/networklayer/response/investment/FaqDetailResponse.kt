package com.emproto.networklayer.response.investment

data class FaqDetailResponse(
    val code: Int,
    val `data`: List<CgData>,
    val message: String
)