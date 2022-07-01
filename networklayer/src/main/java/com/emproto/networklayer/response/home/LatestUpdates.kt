package com.emproto.networklayer.response.home

import java.io.Serializable

data class LatestUpdates(
    val heading: String,
    val subHeading: String
):Serializable