package com.emproto.networklayer.response.home

import com.emproto.networklayer.response.insights.InsightsMedia
import java.io.Serializable

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
):Serializable