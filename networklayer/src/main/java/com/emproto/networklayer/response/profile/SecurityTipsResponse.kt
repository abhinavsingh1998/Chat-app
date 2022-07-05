package com.emproto.networklayer.response.profile

import java.io.Serializable

data class SecurityTipsResponse(
    val code: Int,
    val `data`: StData
):Serializable

data class StData(
    val isFacilityVisible: Boolean,
    val isKycComplete: Boolean,
    val isProfileComplete: Boolean,
    val page: Page,
    val pageManagementsOrNewInvestments: List<Any>
):Serializable

data class Page(
    val aboutUs: AboutUs,
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
    val securityTips: SecurityTips,
    val shortDescription: Any,
    val splashScreenImage: Any,
    val status: String,
    val subHeading: Any,
    val termsAndConditions: TermsAndConditions,
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
    val updatedBy: Any
):Serializable

data class AboutUs(
    val aboutHoabl: AboutHoabl,
    val corporatePhilosophy: CorporatePhilosophy,
    val foundersVision: FoundersVision,
    val isAboutHoablActive: Boolean,
    val isCorporatePhilosophyActive: Boolean,
    val isFoundersVisionActive: Boolean,
    val isProductCategoryActive: Boolean,
    val isStatsOverviewActive: Boolean,
    val productCategory: ProductCategory,
    val statsOverview: StatsOverview
):Serializable

data class SecurityTips(
    val detailedInformation: List<DetailedInformationXXX>,
    val sectionHeading: String
):Serializable

data class TermsAndConditions(
    val description: String,
    val displayName: String
): Serializable

data class AboutHoabl(
    val description: String,
    val sectionHeading: String
):Serializable

data class CorporatePhilosophy(
    val detailedInformation: List<DetailedInformation>,
    val sectionHeading: String
):Serializable

data class FoundersVision(
    val description: String,
    val founderName: String,
    val media: MediaX,
    val sectionHeading: String
):Serializable

data class ProductCategory(
    val detailedInformation: List<DetailedInformationX>,
    val sectionHeading: String
):Serializable

data class StatsOverview(
    val detailedInformation: List<DetailedInformationXX>,
    val sectionHeading: String
):Serializable

data class DetailedInformation(
    val description: String,
    val displayName: String,
    val media: Media
):Serializable

data class Media(
    val key: String,
    val name: String,
    val value: Value
):Serializable

//data class Value(
//    val height: Int,
//    val mediaType: String,
//    val size: Double,
//    val url: String,
//    val width: Int
//)

data class MediaX(
    val key: String,
    val name: String,
    val value: ValueX
):Serializable

//data class ValueX(
//    val height: Int,
//    val mediaType: String,
//    val size: Double,
//    val url: String,
//    val width: Int
//)

data class DetailedInformationX(
    val description: String,
    val displayName: String,
    val media: MediaXX
):Serializable

data class MediaXX(
    val key: String,
    val name: String,
    val value: ValueXX
):Serializable

//data class ValueXX(
//    val height: Int,
//    val mediaType: String,
//    val size: Double,
//    val url: String,
//    val width: Int
//)

data class DetailedInformationXX(
    val description: String,
    val title: String,
    val value: String
):Serializable

data class DetailedInformationXXX(
    val description: String,
    val media: MediaXXX
):Serializable

data class MediaXXX(
    val key: String,
    val name: String,
    val value: ValueXXXD
):Serializable

data class ValueXXXD(
    val height: Int,
    val mediaType: String,
    val size: Double,
    val url: String,
    val width: Int
):Serializable