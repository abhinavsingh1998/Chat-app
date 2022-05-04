package com.emproto.networklayer.response.investment

data class PdProjectTimeline(
    val createdAt: String,
    val id: Int,
    val projectContentId: Int,
    val timeLineSectionHeading: String,
    val timeLines: List<PdTimeLine>,
    val updatedAt: String
)