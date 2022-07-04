package com.emproto.networklayer.response

data class HomeActionItemResponse(
    val code: Int,
    val data: List<Data>,
    val message: String
)