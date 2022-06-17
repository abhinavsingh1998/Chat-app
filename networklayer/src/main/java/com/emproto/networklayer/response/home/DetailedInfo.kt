package com.emproto.networklayer.response.home

import java.io.Serializable

data class DetailedInfo(
    val description: String,
    val media: MediaX
):Serializable