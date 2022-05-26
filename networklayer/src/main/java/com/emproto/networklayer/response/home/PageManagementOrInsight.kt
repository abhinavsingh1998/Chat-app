package com.emproto.networklayer.response.home

import java.io.Serializable

data class PageManagementOrInsight(
    val createdAt: String,
    val createdBy: Int,
    val displayTitle: String,
    val id: Int,
    val insightsMedia: List<InsightsMedia>,
    val modifiedBy: Any,
    val priority: Any,
    val status: String,
    val updatedAt: String
):Serializable