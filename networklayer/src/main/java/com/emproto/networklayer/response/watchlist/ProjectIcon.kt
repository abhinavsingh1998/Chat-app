package com.emproto.networklayer.response.watchlist

import java.io.Serializable

data class ProjectIcon(
    val key: String,
    val name: String,
    val value: Value
):Serializable