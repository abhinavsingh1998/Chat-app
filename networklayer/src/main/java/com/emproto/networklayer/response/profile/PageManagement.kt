package com.emproto.networklayer.response.profile

import java.io.Serializable

data class PageManagement(
    val code: Int,
    val `data`: pData
):Serializable

data class pData(
    val contactType: String,
    val isFacilityVisible: Boolean,
    val isKycComplete: Boolean,
    val isProfileComplete: Boolean,
    val page: ePage,
    val pageManagementsOrNewInvestments: List<Any>
):Serializable

data class ePage(
    val aboutUs: AboutUsX,
    val applicablePromiseSectionHeading: Any,
    val bookingJourney: Any,
    val collectionOne: Any,
    val collectionOneCollectionId: Any,
    val collectionTwo: Any,
    val collectionTwoCollectionId: Any,
    val createdAt: String,
    val createdBy: Int,
    val facilityManagement: Any,
    val faqSectionHeading: String,
    val footerTabs: Any,
    val hoablPromiseDisplayName: Any,
    val id: Int,
    val insightsHeading: Any,
    val insightsSubHeading: Any,
    val isAboutUsActive: Boolean,
    val isCollectionOneActive: Any,
    val isCollectionTwoActive: Any,
    val isFacilityManagementActive: Any,
    val isHoablPromiseActive: Any,
    val isInsightsActive: Any,
    val isLatestUpdatesActive: Any,
    val isMastheadActive: Any,
    val isNewInvestmentsActive: Any,
    val isPromisesActive: Any,
    val isPromotionAndOfferActive: Boolean,
    val isPublished: Boolean,
    val isSecurityTipsActive: Boolean,
    val isTermsActive: Boolean,
    val isTestimonialsActive: Any,
    val latestUpdates: Any,
    val mastheadSection: Any,
    val mythBuster: Any,
    val newInvestments: Any,
    val newInvestmentsWithPriority: Any,
    val numberOfScreens: Any,
    val pageName: String,
    val pageType: String,
    val playTimeInSeconds: Any,
    val promiseSection: Any,
    val promisesHeading: Any,
    val promotionAndOffersMedia: Any,
    val promotionAndOffersProjectContentId: Any,
    val sectionName: Any,
    val securityTips: SecurityTipsX,
    val shortDescription: Any,
    val splashScreenImage: Any,
    val status: Any,
    val subHeading: Any,
    val termsAndConditions: TermsAndConditionsX,
    val testimonialsHeading: Any,
    val testimonialsSubHeading: Any,
    val totalInsightsOnHomeScreen: Any,
    val totalInsightsOnListView: Any,
    val totalProjectsOnHomeScreen: Any,
    val totalPromisesOnHomeScreen: Any,
    val totalTestimonialsOnHomeScreen: Any,
    val totalTestimonialsOnListView: Any,
    val totalUpdatesOnHomeScreen: Any,
    val totalUpdatesOnListView: Any,
    val updatedAt: String,
    val updatedBy: Int
):Serializable

data class TermsAndConditionsX(
    val description: String,
    val displayName: String
): Serializable

data class SecurityTipsX(
    val detailedInformation: List<DetailedInformationXXXE>,
    val sectionHeading: String
):Serializable

data class AboutUsX(
    val aboutHoabl: AboutHoablX,
    val corporatePhilosophy: CorporatePhilosophyX,
    val foundersVision: FoundersVisionX,
    val isAboutHoablActive: Boolean,
    val isCorporatePhilosophyActive: Boolean,
    val isFoundersVisionActive: Boolean,
    val isProductCategoryActive: Boolean,
    val isStatsOverviewActive: Boolean,
    val productCategory: ProductCategoryX,
    val statsOverview: StatsOverviewX
):Serializable

data class DetailedInformationXXXE(
    val description: String,
    val media: MediaXXX
):Serializable

data class AboutHoablX(
    val description: String,
    val sectionHeading: String
):Serializable

data class CorporatePhilosophyX(
    val detailedInformation: List<DetailedInformation>,
    val sectionHeading: String
):Serializable

data class FoundersVisionX(
    val description: String,
    val founderName: String,
    val media: MediaX,
    val sectionHeading: String
):Serializable

data class ProductCategoryX(
    val detailedInformation: List<DetailedInformationX>,
    val sectionHeading: String
):Serializable

data class StatsOverviewX(
    val detailedInformation: List<DetailedInformationXX>,
    val sectionHeading: String
):Serializable