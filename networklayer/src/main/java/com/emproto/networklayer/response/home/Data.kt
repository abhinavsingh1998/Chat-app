package com.emproto.networklayer.response.home

import java.io.Serializable

data class Data(
    val homePagesOrPromises: List<HomePagesOrPromise>,
    val pageManagementsOrNewInvestments: List<PageManagementsOrNewInvestment>,
    val page: Page,
    val pageManagementOrInsights: List<PageManagementOrInsight>,
    val pageManagementsOrNewInvestments: List<PageManagementsOrNewInvestment>,
    val pageManagementOrLatestUpdates: List<PageManagementOrLatestUpdate>,
    val pageManagementsOrTestimonials: List<PageManagementsOrTestimonial>,
    val isKycComplete : Boolean,
    val isProfileComplete : Boolean,
    val isFacilityVisible : Boolean
):Serializable