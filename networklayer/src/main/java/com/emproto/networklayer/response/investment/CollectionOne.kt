package com.emproto.networklayer.response.investment

import java.io.Serializable

data class CollectionOne(
    val heading: String,
    val subHeading: String,
    val totalProjectContentsToDisplay: Int
):Serializable