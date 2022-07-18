package com.emproto.networklayer.response.investment

data class ProjectMediaGalleryResponse(
    val code: Int,
    val `data`: MgData,
    val message: String
)

data class MgData(
    val latestMedaiGalleries: LatestMedaiGalleries,
    val mediaGalleries: MediaGalleries
)

data class LatestMedaiGalleries(
    val coverImage: List<CoverImage>,
    val createdAt: String,
    val droneShoots: List<DroneShoot>,
    val id: Int,
    val images: List<Image>,
    val isCoverImageActive: Boolean,
    val isDroneShootsActive: Boolean,
    val isImagesActive: Boolean,
    val isThreeSixtyImagesActive: Boolean,
    val isVideosActive: Boolean,
    val latestMedaiGalleryProjectContentId: Int,
    val medaiGalleryProjectContentId: Any,
    val threeSixtyImages: List<ThreeSixtyImage>,
    val updatedAt: String,
    val videos: List<Video>
)

data class MediaGalleries(
    val coverImage: List<CoverImage>,
    val images: List<Image>,
    val videos: List<Video>,
    val droneShoots: List<DroneShoot>,

    val createdAt: String,
    val id: Int,
    val isCoverImageActive: Boolean,
    val isDroneShootsActive: Boolean,
    val isImagesActive: Boolean,
    val isThreeSixtyImagesActive: Boolean,
    val isVideosActive: Boolean,
    val latestMedaiGalleryProjectContentId: Any,
    val medaiGalleryProjectContentId: Int,
    val threeSixtyImages: List<ThreeSixtyImage>,
    val updatedAt: String,
)
