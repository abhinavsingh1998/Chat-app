package com.emproto.hoabl.feature.home.data

import com.emproto.networklayer.response.home.PageManagementOrInsight
import com.emproto.networklayer.response.promises.HomePagesOrPromise
import com.emproto.networklayer.response.promises.PromiseSection

data class InsightsData(
    val viewTyppe: Int,
    val data: List<PageManagementOrInsight>
)
