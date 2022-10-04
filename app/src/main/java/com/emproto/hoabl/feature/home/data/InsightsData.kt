package com.emproto.hoabl.feature.home.data

import com.emproto.networklayer.response.home.PageManagementOrInsight

data class InsightsData(
    val viewTyppe: Int,
    val data: List<PageManagementOrInsight>
)
