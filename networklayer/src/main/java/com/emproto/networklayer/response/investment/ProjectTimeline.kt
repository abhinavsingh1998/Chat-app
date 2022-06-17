package com.emproto.networklayer.response.investment

data class ProjectTimeline(
    val createdAt: String,
    val id: Int,
    val projectContentId: Int,
    val timeLineSectionHeading: String,
    val timeLines: List<TimeLine>,
    val updatedAt: String
)