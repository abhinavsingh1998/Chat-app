package com.emproto.networklayer.response.actionItem

data class HomeActionItem(
    val code: Int,
    val `data`: List<Data>,
    val message: String
)