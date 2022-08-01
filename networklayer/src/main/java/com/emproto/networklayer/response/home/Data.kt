package com.emproto.networklayer.response.home

import com.emproto.networklayer.request.notification.UnReadNotifications
import com.emproto.networklayer.response.Data
import com.emproto.networklayer.response.HomeActionItemResponse
import com.emproto.networklayer.response.portfolio.dashboard.PortfolioData
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