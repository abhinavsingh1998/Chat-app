package com.emproto.networklayer.request.investment

data class VideoCallBody(
    val caseType: String,
    val description: String,
    val issueType: String,
    val projectId: Int
)