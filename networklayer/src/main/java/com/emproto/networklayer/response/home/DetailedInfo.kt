package com.emproto.networklayer.response.home

import com.emproto.networklayer.response.responsee.Media

data class DetailedInfo(
    val description: String,
    val media: Media
)