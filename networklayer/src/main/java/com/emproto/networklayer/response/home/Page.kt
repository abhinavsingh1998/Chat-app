package com.emproto.networklayer.response.home

import java.io.Serializable

data class Page(
    val aboutUs: Any,
    val bookingJourney: Any,
    val collectionOne: Any,
    val collectionOneCollectionId: Any,
    val collectionTwo: Any,
    val collectionTwoCollectionId: Any,
    val createdAt: String,
    val facilityManagement: FacilityManagement,
    val footerTabs: Any,
    val hoablPromiseDisplayName: Any,
    val id: Int,
    val insightsHeading: String,
    val insightsSubHeading: String,
    val isAboutUsActive: Any,
    val isCollectionOneActive: Any,
    val isCollectionTwoActive: Any,
    val isFacilityManagementActive: Boolean,
    val isHoablPromiseActive: Any,
    val isInsightsActive: Boolean,
    val isLatestUpdatesActive: Boolean,
    val isMastheadActive: Boolean,
    val isNewInvestmentsActive: Boolean,
    val isPromisesActive: Boolean,
    val isPublished: Boolean,
    val isSecurityTipsActive: Any,
    val isTermsActive: Any,
    val isTestimonialsActive: Boolean,
    val latestUpdates: LatestUpdates,
    val mastheadSection: MastheadSection,
    val mythBuster: Any,
    val newInvestments: NewInvestments,
    val numberOfScreens: Any,
    val pageManagementsOrCollectionOneModels: List<Any>,
    val pageManagementsOrCollectionTwoModels: List<Any>,
    val pageManagementsOrNewInvestments: List<PageManagementsOrNewInvestment>,
    val pageName: String,
    val pageType: String,
    val playTimeInSeconds: Any,
    val promiseSection: Any,
    val promisesHeading: String,
    val promotionAndOffersMedia: Any,
    val promotionAndOffersProjectContentId: Int,
    val propositionScreens: Any,
    val sectionName: Any,
    val securityTips: Any,
    val shortDescription: Any,
    val splashScreenImage: Any,
    val subHeading: Any,
    val termsAndConditions: Any,
    val testimonialsHeading: String,
    val testimonialsSubHeading: String,
    val totalInsightsOnHomeScreen: Int,
    val totalInsightsOnListView: Int,
    val totalProjectsOnHomeScreen: Int,
    val totalPromisesOnHomeScreen: Int,
    val totalTestimonialsOnHomeScreen: Int,
    val totalTestimonialsOnListView: Int,
    val totalUpdatesOnHomeScreen: Int,
    val totalUpdatesOnListView: Int,
    val updatedAt: String
):Serializable