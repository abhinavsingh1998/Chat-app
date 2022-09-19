package com.emproto.networklayer.response.home

import java.io.Serializable

data class Data(
    val homePagesOrPromises: List<HomePagesOrPromise>,
    val pageManagementsOrNewInvestments: List<PageManagementsOrNewInvestment>,
    val page: Page,
    val pageManagementOrInsights: List<PageManagementOrInsight>,
    val pageManagementOrLatestUpdates: List<PageManagementOrLatestUpdate>,
    val pageManagementsOrTestimonials: List<PageManagementsOrTestimonial>,
    val isKycComplete : Boolean,
    val isProfileComplete : Boolean,
    val contactType: Int,
    val isFacilityVisible : Boolean,
    var actionItem: List<com.emproto.networklayer.response.actionItem.Data>,
    var notifications: List<com.emproto.networklayer.response.notification.dataResponse.Data>,
    var portfolioData:PortFolioData
):Serializable