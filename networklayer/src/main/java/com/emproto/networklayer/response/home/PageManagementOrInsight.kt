package com.emproto.networklayer.response.home

data class PageManagementOrInsight(
    val createdAt: String,
    val createdBy: Int,
    val displayTitle: String,
    val id: Int,
    val insightMedia: InsightMedia,
    val modifiedBy: Int,
    val pageManagementAndInsights: PageManagementAndInsights,
    val status: Boolean,
    val subTitle: String,
    val updatedAt: String
)