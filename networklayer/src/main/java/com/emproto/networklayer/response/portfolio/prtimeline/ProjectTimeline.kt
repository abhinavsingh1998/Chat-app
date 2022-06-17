package com.emproto.networklayer.response.portfolio.prtimeline

import com.emproto.networklayer.response.investment.ReraDetails

data class ProjectTimeline(
    val createdAt: String,
    val id: Int,
    val projectContentId: Int,
    val timeLineSectionHeading: String,
    val timeLines: List<TimeLine>,
    val updatedAt: String,
    var reraDetails: ReraDetails
)