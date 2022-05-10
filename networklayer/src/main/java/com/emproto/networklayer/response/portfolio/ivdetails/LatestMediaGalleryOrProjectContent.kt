package com.emproto.networklayer.response.portfolio.ivdetails

data class LatestMediaGalleryOrProjectContent(
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
    val latestMedaiGalleryProjectCon: Int,
    val medaiGalleryProjectContentId: Any,
    val threeSixtyImages: List<ThreeSixtyImage>,
    val updatedAt: String,
    val videos: List<Video>
)