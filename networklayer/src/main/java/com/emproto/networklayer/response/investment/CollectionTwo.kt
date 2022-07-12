package com.emproto.networklayer.response.investment

import java.io.Serializable

data class CollectionTwo(
    val heading: String,
    val subHeading: String,
    val totalProjectContentsToDisplay: Int
): Serializable