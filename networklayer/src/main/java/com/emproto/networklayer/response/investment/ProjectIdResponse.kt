package com.emproto.networklayer.response.investment

data class ProjectIdResponse(
    val code: Int,
    val `data`: ProjectDetailData
)


// data class Data(
// val address: Address,
// val areaRange: AreaRange,
// val createdAt: String,
// val fomoContent: FomoContent,
// val generalInfoEscalationGraph: GeneralInfoEscalationGraph,
// val id: Int,
// val inventoryBucketContents: List<InventoryBucketContent>,
// val isEscalationGraphActive: Boolean,
// val isInventoryBucketActive: Boolean,
// val isKeyPillarsActive: Boolean,
// val isLocationInfrastructureActive: Boolean,
// val isOffersAndPromotionsActive: Boolean,
// val keyPillars: KeyPillars,
// val launchName: String,
// val locationInfrastructure: LocationInfrastructure,
// val mediaGalleries: List<MediaGallery>,
// val offersAndPromotions: OffersAndPromotions,
// val opprotunityDocs: List<OpprotunityDoc>,
// val priceRange: PriceRangeX,
// val projectId: Int,
// val projectTimelines: List<ProjectTimeline>,
// val shortDescription: String,
// val status: String,
// val updatedAt: String
// )
//
// data class Address(
// val city: String,
// val country: String,
// val gpsLocationLink: String,
// val pinCode: String,
// val state: String
// )
//
// data class AreaRange(
// val from: String,
// val to: String
// )
//
// data class FomoContent(
// val days: Int,
// val isDaysActive: Boolean,
// val isNoOfViewsActive: Boolean,
// val isTargetTimeActive: Boolean,
// val noOfViews: Int,
// val targetTime: TargetTime
// )
//
// data class GeneralInfoEscalationGraph(
// val dataPoints: DataPoints,
// val estimatedAppreciation: Int,
// val xAxisDisplayName: String,
// val yAxisDisplayName: String
// )
//
// data class InventoryBucketContent(
// val areaRange: AreaRangeX,
// val createdAt: String,
// val id: Int,
// val inventoryBucketId: Int,
// val name: String,
// val priceRange: PriceRange,
// val projectContentId: Int,
// val shortDescription: String,
// val updatedAt: String
// )
//
// data class KeyPillars(
// val heading: String,
// val values: List<Value>
// )
//
// data class LocationInfrastructure(
// val heading: String,
// val values: List<ValueXX>
// )
//
// data class MediaGallery(
// val coverImage: List<CoverImage>,
// val createdAt: String,
// val droneShoots: List<DroneShoot>,
// val id: Int,
// val images: List<Image>,
// val isCoverImageActive: Boolean,
// val isDroneShootsActive: Boolean,
// val isImagesActive: Boolean,
// val isThreeSixtyImagesActive: Boolean,
// val isVideosActive: Boolean,
// val projectContentId: Int,
// val threeSixtyImages: List<ThreeSixtyImage>,
// val updatedAt: String,
// val videos: List<Video>
// )
//
// data class OffersAndPromotions(
// val key: String,
// val name: String,
// val value: ValueXXXXXXXXX
// )
//
// data class OpprotunityDoc(
// val aboutProjects: AboutProjects,
// val bannerImage: BannerImage,
// val createdAt: String,
// val currentInfraStory: CurrentInfraStory,
// val escalationGraph: EscalationGraph,
// val id: Int,
// val isAboutProjectsActive: Boolean,
// val isBannerImageActive: Boolean,
// val isCurrentInfraStoryActive: Boolean,
// val isEscalationGraphActive: Boolean,
// val isPageFooterActive: Boolean,
// val isProjectAminitiesActive: Boolean,
// val isTourismAroundActive: Boolean,
// val isUpcomingInfraStoryActive: Boolean,
// val pageFooter: String,
// val projectAminities: List<ProjectAminity>,
// val projectContentId: Int,
// val tourismAround: TourismAround,
// val upcomingInfraStory: UpcomingInfraStory,
// val updatedAt: String
// )
//
// data class PriceRangeX(
// val from: String,
// val to: String
// )
//
// data class ProjectTimeline(
// val createdAt: String,
// val id: Int,
// val projectContentId: Int,
// val timeLineSectionHeading: String,
// val timeLines: List<TimeLine>,
// val updatedAt: String
// )
//
// data class TargetTime(
// val hours: Int,
// val minutes: Int,
// val seconds: Int
// )
//
// data class DataPoints(
// val dataPointType: String,
// val points: List<Point>
// )
//
// data class Point(
// val halfYear: Any,
// val month: Any,
// val quater: Any,
// val value: Int,
// val year: String
// )
//
// data class AreaRangeX(
// val from: String,
// val to: String
// )
//
// data class PriceRange(
// val from: String,
// val to: String
// )
//
// data class Value(
// val icon: Icon,
// val name: String,
// val points: List<String>
// )
//
// data class Icon(
// val key: String,
// val name: String,
// val value: ValueX
// )
//
// data class ValueX(
// val height: Int,
// val mediaType: String,
// val size: Double,
// val url: String,
// val width: Int
// )
//
// data class ValueXX(
// val gpsLink: String,
// val icon: IconX,
// val name: String
// )
//
// data class IconX(
// val key: String,
// val name: String,
// val value: ValueXXX
// )
//
// data class ValueXXX(
// val height: Int,
// val mediaType: String,
// val size: Double,
// val url: String,
// val width: Int
// )
//
// data class CoverImage(
// val mediaContent: MediaContent,
// val mediaContentType: String,
// val name: String,
// val status: String
// )
//
// data class DroneShoot(
// val mediaContent: MediaContentX,
// val mediaContentType: String,
// val name: String,
// val status: String
// )
//
// data class Image(
// val mediaContent: MediaContentXX,
// val mediaContentType: String,
// val name: String,
// val status: String
// )
//
// data class ThreeSixtyImage(
// val mediaContent: MediaContentXXX,
// val mediaContentType: String,
// val name: String,
// val status: String
// )
//
// data class Video(
// val mediaContent: MediaContentXXXX,
// val mediaContentType: String,
// val name: String,
// val status: String
// )
//
// data class MediaContent(
// val key: String,
// val name: String,
// val value: ValueXXXX
// )
//
// data class ValueXXXX(
// val height: Int,
// val mediaType: String,
// val size: Double,
// val url: String,
// val width: Int
// )
//
// data class MediaContentX(
// val key: String,
// val name: String,
// val value: ValueXXXXX
// )
//
// data class ValueXXXXX(
// val height: Int,
// val mediaType: String,
// val size: Double,
// val url: String,
// val width: Int
// )
//
// data class MediaContentXX(
// val key: String,
// val name: String,
// val value: ValueXXXXXX
// )
//
// data class ValueXXXXXX(
// val height: Int,
// val mediaType: String,
// val size: Double,
// val url: String,
// val width: Int
// )
//
// data class MediaContentXXX(
// val key: String,
// val name: String,
// val value: ValueXXXXXXX
// )
//
// data class ValueXXXXXXX(
// val height: Int,
// val mediaType: String,
// val size: Double,
// val url: String,
// val width: Int
// )
//
// data class MediaContentXXXX(
// val key: String,
// val name: String,
// val value: ValueXXXXXXXX
// )
//
// data class ValueXXXXXXXX(
// val height: Int,
// val mediaType: String,
// val size: Double,
// val url: String,
// val width: Int
// )
//
// data class ValueXXXXXXXXX(
// val height: Int,
// val mediaType: String,
// val size: Double,
// val url: String,
// val width: Int
// )
//
// data class AboutProjects(
// val description: String,
// val heading: String,
// val media: Media
// )
//
// data class BannerImage(
// val key: String,
// val name: String,
// val value: ValueXXXXXXXXXXX
// )
//
// data class CurrentInfraStory(
// val heading: String,
// val stories: List<Story>
// )
//
// data class EscalationGraph(
// val dataPoints: DataPointsX,
// val projectEstimatedAppreciation: Int,
// val xAxisDisplayName: String,
// val yAxisDisplayName: String
// )
//
// data class ProjectAminity(
// val icon: IconXX,
// val name: String
// )
//
// data class TourismAround(
// val heading: String,
// val stories: List<StoryX>
// )
//
// data class UpcomingInfraStory(
// val heading: String,
// val stories: List<StoryXX>
// )
//
// data class Media(
// val key: String,
// val name: String,
// val value: ValueXXXXXXXXXX
// )
//
// data class ValueXXXXXXXXXX(
// val height: Int,
// val mediaType: String,
// val size: Double,
// val url: String,
// val width: Int
// )
//
// data class ValueXXXXXXXXXXX(
// val height: Int,
// val mediaType: String,
// val size: Double,
// val url: String,
// val width: Int
// )
//
// data class Story(
// val description: String,
// val media: MediaX,
// val title: String
// )
//
// data class MediaX(
// val key: String,
// val name: String,
// val value: ValueXXXXXXXXXXXX
// )
//
// data class ValueXXXXXXXXXXXX(
// val height: Int,
// val mediaType: String,
// val size: Double,
// val url: String,
// val width: Int
// )
//
// data class DataPointsX(
// val dataPointType: String,
// val points: List<PointX>
// )
//
// data class PointX(
// val halfYear: Any,
// val month: Any,
// val quater: Any,
// val value: Int,
// val year: String
// )
//
// data class IconXX(
// val key: String,
// val name: String,
// val value: ValueXXXXXXXXXXXXX
// )
//
// data class ValueXXXXXXXXXXXXX(
// val height: Int,
// val mediaType: String,
// val size: Double,
// val url: String,
// val width: Int
// )
//
// data class StoryX(
// val description: String,
// val media: MediaXX,
// val title: String
// )
//
// data class MediaXX(
// val key: String,
// val name: String,
// val value: ValueXXXXXXXXXXXXXX
// )
//
// data class ValueXXXXXXXXXXXXXX(
// val height: Int,
// val mediaType: String,
// val size: Double,
// val url: String,
// val width: Int
// )
//
// data class StoryXX(
// val description: String,
// val media: MediaXXX,
// val title: String
// )
//
// data class MediaXXX(
// val key: String,
// val name: String,
// val value: ValueXXXXXXXXXXXXXXX
// )
//
// data class ValueXXXXXXXXXXXXXXX(
// val height: Int,
// val mediaType: String,
// val size: Double,
// val url: String,
// val width: Int
// )
//
// data class TimeLine(
// val heading: String,
// val sections: List<Section>
// )
//
// data class Section(
// val key: String,
// val values: Values
// )
//
// data class Values(
// val dateOfCompletion: String,
// val displayName: String,
// val medias: Medias,
// val percentage: Int,
// val status: String,
// val toolTipDetails: String,
// val webLink: String
// )
//
// data class Medias(
// val key: String,
// val name: String,
// val value: ValueXXXXXXXXXXXXXXXX
// )
//
// data class ValueXXXXXXXXXXXXXXXX(
// val height: Int,
// val mediaType: String,
// val size: Double,
// val url: String,
// val width: Int
// )
//
