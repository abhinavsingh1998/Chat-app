package com.emproto.networklayer.response.home

import java.io.Serializable

data class PromiseMedia(
    val key: String,
    val name: String,
    val value: ValueX
):Serializable