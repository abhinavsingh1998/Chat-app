package com.emproto.networklayer.response.responsee

data class Data(
    val homePagesOrPromises: List<HomePagesOrPromise>,
    val page: Page,
    val pageManagementOrInsights: List<PageManagementOrInsight>,
    val pageManagementOrLatestUpdates: List<PageManagementOrLatestUpdate>,
    val pageManagementsOrTestimonials: List<PageManagementsOrTestimonial>
)