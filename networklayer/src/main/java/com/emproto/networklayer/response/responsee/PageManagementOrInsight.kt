package com.emproto.networklayer.response.responsee

data class PageManagementOrInsight(
    val createdAt: String,
    val createdBy: Int,
    val displayTitle: String,
    val id: Int,
    val insightsMedia: InsightsMedia,
    val modifiedBy: Int,
    val pageManagementAndInsights: PageManagementAndInsights,
    val priority: Any,
    val status: Boolean,
    val updatedAt: String
)