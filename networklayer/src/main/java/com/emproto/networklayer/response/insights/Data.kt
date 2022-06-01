package com.emproto.networklayer.response.insights

data class Data(
    val createdAt: String,
    val createdBy: Int,
    val displayTitle: String,
    val id: Int,
    val insightsCreatedAdmin: InsightsCreatedAdmin,
    val insightsMedia: List<InsightsMedia>,
    val insightsModifiedAdmin: InsightsModifiedAdmin,
    val modifiedBy: Int,
    val priority: Int,
    val status: String,
    val updatedAt: String
)