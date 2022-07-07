package com.emproto.networklayer.response.search

import com.emproto.networklayer.response.insights.InsightsCreatedAdmin
import com.emproto.networklayer.response.insights.InsightsModifiedAdmin
import com.emproto.networklayer.response.investment.ApData
import com.emproto.networklayer.response.portfolio.ivdetails.FrequentlyAskedQuestion

data class SearchResponse(
    val code: Int,
    val `data`: Data,
    val message: String
)

data class Data(
    val faqData: List<FrequentlyAskedQuestion>,
    val insightsData: List<com.emproto.networklayer.response.insights.Data>,
    val marketingUpdateData: List<com.emproto.networklayer.response.marketingUpdates.Data>,
    val projectContentData: List<ApData>,
    var docsData: List<com.emproto.networklayer.response.documents.Data>
)

data class InsightsData(
    val createdAt: String,
    val createdBy: Int,
    val displayTitle: String,
    val id: Int,
    val insightsMedia: List<InsightsMedia>,
    val modifiedBy: Int,
    val priority: Int,
    val status: String,
    val updatedAt: String
)

data class MarketingUpdateData(
    val createdAt: String,
    val createdBy: Int,
    val detailedInfo: List<DetailedInfo>,
    val displayTitle: String,
    val formType: String,
    val id: Int,
    val priority: Int,
    val shouldDisplayDate: Boolean,
    val status: String,
    val subTitle: String,
    val updateType: String,
    val updatedAt: String,
    val updatedBy: Int
)

data class ProjectContentData(
    val address: Address,
    val areaStartingFrom: Any,
    val createdAt: String,
    val createdBy: Any,
    val crmLaunchPhaseId: Int,
    val crmProjectId: Int,
    val fomoContent: FomoContent,
    val generalInfoEscalationGraph: GeneralInfoEscalationGraph,
    val id: Int,
    val isEscalationGraphActive: Boolean,
    val isInventoryBucketActive: Boolean,
    val isKeyPillarsActive: Boolean,
    val isLatestMediaGalleryActive: Boolean,
    val isLocationInfrastructureActive: Boolean,
    val isOffersAndPromotionsActive: Boolean,
    val keyPillars: KeyPillars,
    val latestMediaGalleryHeading: String,
    val launchName: String,
    val locationInfrastructure: LocationInfrastructure,
    val numberOfSimilarInvestmentsToShow: Any,
    val offersAndPromotions: OffersAndPromotions,
    val priceStartingFrom: Any,
    val projectCoverImages: ProjectCoverImages,
    val reraDetails: ReraDetails,
    val shortDescription: String,
    val status: String,
    val updatedAt: String,
    val updatedBy: Any
)

data class FaqAnswer(
    val answer: String
)

data class FaqQuestion(
    val question: String
)

data class InsightsMedia(
    val description: String,
    val media: Media
)

data class Media(
    val isActive: Boolean,
    val key: String,
    val mediaDescription: String,
    val name: String,
    val value: Value
)

data class Value(
    val height: Int,
    val imageDialogUrl: String,
    val isCancel: Boolean,
    val isPreview: Boolean,
    val mainUrl: String,
    val mediaType: String,
    val size: Int,
    val successFullyUploadedType: String,
    val url: String,
    val videoDialogUrl: String,
    val width: Int
)

data class DetailedInfo(
    val description: String,
    val media: MediaX
)

data class MediaX(
    val isActive: Boolean,
    val key: String,
    val mediaType: String,
    val name: String,
    val value: ValueX
)

data class ValueX(
    val mediaType: String,
    val url: String
)

data class Address(
    val city: String,
    val country: String,
    val gpsLocationLink: String,
    val mapMedia: MapMedia,
    val pinCode: String,
    val state: String
)

data class FomoContent(
    val days: Int,
    val isDaysActive: Boolean,
    val isNoOfViewsActive: Boolean,
    val isTargetTimeActive: Boolean,
    val noOfViews: Int,
    val targetTime: TargetTime
)

data class GeneralInfoEscalationGraph(
    val dataPoints: DataPoints,
    val estimatedAppreciation: Double,
    val title: String,
    val xAxisDisplayName: String,
    val yAxisDisplayName: String
)

data class KeyPillars(
    val heading: String,
    val values: List<ValueXXX>
)

data class LocationInfrastructure(
    val heading: String,
    val values: List<ValueXXXXX>
)

data class OffersAndPromotions(
    val key: String,
    val name: String,
    val value: ValueXXXXXXX
)

data class ProjectCoverImages(
    val chatListViewPageMedia: ChatListViewPageMedia,
    val chatPageMedia: ChatPageMedia,
    val collectionListViewPageMedia: CollectionListViewPageMedia,
    val homePageMedia: HomePageMedia,
    val newInvestmentPageMedia: NewInvestmentPageMedia,
    val portfolioPageMedia: PortfolioPageMedia
)

data class ReraDetails(
    val companyNameAndAddress: String,
    val reraNumbers: List<String>
)

data class MapMedia(
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

data class TargetTime(
    val hours: Int,
    val minutes: Int,
    val seconds: Int
)

data class DataPoints(
    val dataPointType: String,
    val points: List<Point>
)

data class Point(
    val halfYear: Any,
    val month: Any,
    val quater: Any,
    val value: Int,
    val year: String
)

data class ValueXXX(
    val icon: Icon,
    val name: String,
    val points: List<String>
)

data class Icon(
    val key: String,
    val name: String,
    val value: ValueXXXX
)

data class ValueXXXX(
    val height: Int,
    val mediaType: String,
    val size: Double,
    val url: String,
    val width: Int
)

data class ValueXXXXX(
    val icon: IconX,
    val latitude: Double,
    val longitude: Double,
    val name: String
)

data class IconX(
    val key: String,
    val name: String,
    val value: ValueXXXXXX
)

data class ValueXXXXXX(
    val height: Int,
    val mediaType: String,
    val size: Double,
    val url: String,
    val width: Int
)

data class ValueXXXXXXX(
    val height: Int,
    val mediaType: String,
    val size: Double,
    val url: String,
    val width: Int
)

data class ChatListViewPageMedia(
    val key: String,
    val name: String,
    val value: ValueXXXXXXXX
)

data class ChatPageMedia(
    val key: String,
    val name: String,
    val value: ValueXXXXXXXXX
)

data class CollectionListViewPageMedia(
    val key: String,
    val name: String,
    val value: ValueXXXXXXXXXX
)

data class HomePageMedia(
    val key: String,
    val name: String,
    val value: ValueXXXXXXXXXXX
)

data class NewInvestmentPageMedia(
    val key: String,
    val name: String,
    val value: ValueXXXXXXXXXXXX
)

data class PortfolioPageMedia(
    val key: String,
    val name: String,
    val value: ValueXXXXXXXXXXXXX
)

data class ValueXXXXXXXX(
    val height: Int,
    val mediaType: String,
    val size: Double,
    val url: String,
    val width: Int
)

data class ValueXXXXXXXXX(
    val height: Int,
    val mediaType: String,
    val size: Double,
    val url: String,
    val width: Int
)

data class ValueXXXXXXXXXX(
    val height: Int,
    val mediaType: String,
    val size: Double,
    val url: String,
    val width: Int
)

data class ValueXXXXXXXXXXX(
    val height: Int,
    val mediaType: String,
    val size: Double,
    val url: String,
    val width: Int
)

data class ValueXXXXXXXXXXXX(
    val height: Int,
    val mediaType: String,
    val size: Double,
    val url: String,
    val width: Int
)

data class ValueXXXXXXXXXXXXX(
    val height: Int,
    val mediaType: String,
    val size: Double,
    val url: String,
    val width: Int
)