package com.emproto.networklayer.response.responsee

data class PageManagementOrInsight(
    val createdAt: String,
    val createdBy: Int,
    val displayTitle: String,
    val id: Int,
    val insightsMedia: List<InsightsMedia>,
    val modifiedBy: Int,
    val priority: Int,
    val status: String,
    val updatedAt: String
)