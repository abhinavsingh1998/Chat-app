package com.emproto.networklayer.response.portfolio.prtimeline

data class ProjectTimelineResponse(
    val code: Int,
    val `data`: ProjectContent
)

data class ProjectContent(val projectContent: Data)