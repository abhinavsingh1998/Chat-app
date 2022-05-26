package com.emproto.networklayer.response.home

import java.io.Serializable

data class DisplayMedia(
    val key: String,
    val name: String,
    val value: Value
):Serializable