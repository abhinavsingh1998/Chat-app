package com.emproto.networklayer.response.home

import java.io.Serializable

data class InsightsMedia(
    val description: String,
    val media: Media
):Serializable