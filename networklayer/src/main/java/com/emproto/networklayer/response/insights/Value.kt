package com.emproto.networklayer.response.insights

data class Value(
    val height: Int,
    val isPreview: Boolean,
    val mediaType: String,
    val preview: String,
    val size: Int,
    val url: String,
    val width: Int
)