package com.emproto.networklayer.response.home

import java.io.Serializable

data class Data(
    val homePagesOrPromises: List<HomePagesOrPromise>,
    val page: Page,
    val pageManagementOrInsights: List<PageManagementOrInsight>,
    val pageManagementOrLatestUpdates: List<PageManagementOrLatestUpdate>,
    val pageManagementsOrTestimonials: List<PageManagementsOrTestimonial>
):Serializable