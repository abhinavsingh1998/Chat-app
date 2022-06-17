package com.emproto.networklayer.response.investment

data class ProjectContentsAndSimilarinvestments(
    val created: String,
    val id: Int,
    val project: Int,
    val similar: Int,
    val updated: String
)