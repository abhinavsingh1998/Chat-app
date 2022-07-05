package com.emproto.networklayer.response

import com.emproto.networklayer.response.actionItem.Data

data class HomeActionItemResponse(
    val code: Int,
    val `data`: List<Data>,
    val message: String
)