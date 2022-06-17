package com.emproto.networklayer.response.marketingUpdates

data class Media(
    val isActive: Boolean,
    val key: String,
    val mediaType: String,
    val name: String,
    val value: Value
)