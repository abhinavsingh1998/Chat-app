package com.emproto.networklayer.response.profile

data class SecurityTipsResponse(
    val code: Int,
    val `data`: StData
)

data class StData(
    val isFacilityVisible: Boolean,
    val isKycComplete: Boolean,
    val isProfileComplete: Boolean,
    val page: Page,
    val pageManagementsOrNewInvestments: List<Any>
)

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
)

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
)

data class SecurityTips(
    val detailedInformation: List<DetailedInformationXXX>,
    val sectionHeading: String
)

data class TermsAndConditions(
    val description: String,
    val displayName: String
)

data class AboutHoabl(
    val description: String,
    val sectionHeading: String
)

data class CorporatePhilosophy(
    val detailedInformation: List<DetailedInformation>,
    val sectionHeading: String
)

data class FoundersVision(
    val description: String,
    val founderName: String,
    val media: MediaX,
    val sectionHeading: String
)

data class ProductCategory(
    val detailedInformation: List<DetailedInformationX>,
    val sectionHeading: String
)

data class StatsOverview(
    val detailedInformation: List<DetailedInformationXX>,
    val sectionHeading: String
)

data class DetailedInformation(
    val description: String,
    val displayName: String,
    val media: Media
)

data class Media(
    val key: String,
    val name: String,
    val value: Value
)

data class Value(
    val height: Int,
    val mediaType: String,
    val size: Double,
    val url: String,
    val width: Int
)

data class MediaX(
    val key: String,
    val name: String,
    val value: ValueX
)

data class ValueX(
    val height: Int,
    val mediaType: String,
    val size: Double,
    val url: String,
    val width: Int
)

data class DetailedInformationX(
    val description: String,
    val displayName: String,
    val media: MediaXX
)

data class MediaXX(
    val key: String,
    val name: String,
    val value: ValueXX
)

data class ValueXX(
    val height: Int,
    val mediaType: String,
    val size: Double,
    val url: String,
    val width: Int
)

data class DetailedInformationXX(
    val description: String,
    val title: String,
    val value: String
)

data class DetailedInformationXXX(
    val description: String,
    val media: MediaXXX
)

data class MediaXXX(
    val key: String,
    val name: String,
    val value: ValueXXX
)

data class ValueXXX(
    val height: Int,
    val mediaType: String,
    val size: Double,
    val url: String,
    val width: Int
)