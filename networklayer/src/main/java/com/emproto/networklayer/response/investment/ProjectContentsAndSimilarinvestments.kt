package com.emproto.networklayer.response.investment

import java.io.Serializable

data class ProjectContentsAndSimilarinvestments(
    val created: String,
    val id: Int,
    val project: Int,
    val similar: Int,
    val updated: String
): Serializable